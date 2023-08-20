package com.example.nlearn.services;

import com.example.nlearn.exception.TestNotAllowedException;
import com.example.nlearn.dtos.QuestionForStudentDto;
import com.example.nlearn.dtos.TestDto;
import com.example.nlearn.models.Mark;
import com.example.nlearn.models.Question;
import com.example.nlearn.models.Test;
import com.example.nlearn.models.TestSessionInfo;
import com.example.nlearn.models.User;
import com.example.nlearn.records.FullTestForPassing;
import com.example.nlearn.repos.TestSessionInfoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.StreamUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@CrossOrigin
public class TestPassingService {

    private static final int INSURANCE_TIME = 2;
    private static final int SCALE_OF_MARKS = 100;

    TestSessionInfoRepository testSessionInfoRepository;
    private final TestService testService;
    private final QuestionService questionService;
    private final MarkService markService;
    private final ModelMapper modelMapper;

    public TestPassingService(TestSessionInfoRepository testSessionInfoRepository, TestService testService, QuestionService questionService, MarkService markService, ModelMapper modelMapper) {
        this.testSessionInfoRepository = testSessionInfoRepository;
        this.testService = testService;
        this.questionService = questionService;
        this.markService = markService;
        this.modelMapper = modelMapper;
    }

    public void raiseErrorIfTestingNotAllowed(Test test, int testId, User user, TestSessionInfo sessionInfo, LocalDateTime currentTime, LocalDateTime endTime) {
        boolean isTestForThisStudent = test.getGroup().getId() == user.getGroup().getId();
        boolean areRetriesOver = markService.countMarksByUserAndTest(user, test) >= test.getNumberOfRetries();
        if ((sessionInfo.isActive() && sessionInfo.getCurrentTestId() != testId) || !isTestForThisStudent || endTime.isBefore(currentTime) || areRetriesOver) {
            throw new TestNotAllowedException();
        }
    }


    public FullTestForPassing startTest(Integer testId, User user) {
        Test test = testService.getTestById(testId);
        TestSessionInfo sessionInfo = user.getTestSessionInfo();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime endTime = test.getEndTime();

        raiseErrorIfTestingNotAllowed(test, testId, user, sessionInfo, currentTime, endTime);

        List<Question> questions = questionService.findByTestId(testId);


        if (sessionInfo.isActive()) {
            test.setEndTime(sessionInfo.getEndTime().minusMinutes(INSURANCE_TIME));
        } else {
            int duration = (int) Math.min(test.getDuration(), ChronoUnit.MINUTES.between(currentTime, endTime));
            LocalDateTime sessionEndTime = currentTime.plusMinutes(duration);
            test.setEndTime(sessionEndTime);
            sessionInfo.startSession(test.getId(), sessionEndTime.plusMinutes(INSURANCE_TIME));
            testSessionInfoRepository.save(sessionInfo);
        }

        return new FullTestForPassing(modelMapper.map(test, TestDto.class), questions.stream().map( question -> modelMapper.map(question, QuestionForStudentDto.class)).toList());
    }


    public ResponseEntity endTest(Integer testId, User user, List<Question> answeredQuestions) {
        Test test = testService.getTestById(testId);
        TestSessionInfo sessionInfo = user.getTestSessionInfo();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime endTime = sessionInfo.getEndTime();

        boolean isTestAllowed = test.getGroup().getId() == user.getGroup().getId();
        if (!sessionInfo.isActive() || !isTestAllowed || !currentTime.isBefore(endTime)) {
            return ResponseEntity.notFound().build();
        }

        List<Question> questions = questionService.findByTestId(testId);

        double testValuation = StreamUtils.zip(answeredQuestions.stream(), questions.stream(), (answered, real) ->
                answered.getCorrectIndexes().size() > real.getCorrectIndexes().size()
                        ? 0.0
                        : answered.getCorrectIndexes().stream().mapToDouble(integer -> {
                    if (real.getCorrectIndexes().contains(integer)) {
                        return 1;
                    }
                    return 0;

                }).sum() / real.getNumberOfCorrectAnswers()).mapToDouble(Double::doubleValue).sum();

        Mark mark = new Mark();
        mark.setValue((int) (testValuation * SCALE_OF_MARKS / questions.size()));
        mark.setTest(test);
        mark.setUser(user);
        markService.save(mark);

        sessionInfo.setActive(false);
        testSessionInfoRepository.save(sessionInfo);

        return ResponseEntity.ok(questions);
    }

    public void checkIfAllTestsClosed(User user) {
        TestSessionInfo sessionInfo = user.getTestSessionInfo();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime endTime = sessionInfo.getEndTime();

        if (sessionInfo.isActive() && currentTime.isAfter(endTime)) {
            Mark mark = new Mark();
            mark.setValue(0);
            mark.setTest(testService.getTestById(sessionInfo.getCurrentTestId()));
            mark.setUser(user);
            markService.save(mark);

            sessionInfo.setActive(false);
            testSessionInfoRepository.save(sessionInfo);
        }
    }
}
