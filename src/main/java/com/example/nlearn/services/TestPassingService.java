package com.example.nlearn.services;

import com.example.nlearn.models.FullTest;
import com.example.nlearn.models.Mark;
import com.example.nlearn.models.Question;
import com.example.nlearn.models.Test;
import com.example.nlearn.models.TestSessionInfo;
import com.example.nlearn.models.User;
import com.example.nlearn.repos.TestSessionInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.StreamUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@CrossOrigin
public class TestPassingService {

    private static final int INSURANCE_TIME = 2;
    private static final int SCALE_OF_MARKS = 100;
    @Autowired
    TestSessionInfoRepository testSessionInfoRepository;

    @Autowired
    private TestService testRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private MarkService markService;


    public void raiseErrorIfTestingNotAllowed(Test test, int testId, User user, TestSessionInfo sessionInfo, LocalDateTime currentTime, LocalDateTime endTime) {
        boolean isTestForThisStudent = test.getGroup().getId() == user.getGroup().getId();
        boolean areRetriesOver = markService.countMarksByUserAndTest(user, test) >= test.getNumberOfRetries();
        if ((sessionInfo.isActive() && sessionInfo.getCurrentTestId() != testId) || !isTestForThisStudent || endTime.isBefore(currentTime) || areRetriesOver) {
            throw new ResourceAccessException("");
        }
    }


    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public FullTest startTest(Integer testId, User user) {
        Test test = testRepository.getTestById(testId);
        TestSessionInfo sessionInfo = user.getTestSessionInfo();
        LocalDateTime currentTime = java.time.LocalDateTime.now();
        LocalDateTime endTime = test.getEndTime();

        raiseErrorIfTestingNotAllowed(test, testId, user, sessionInfo, currentTime, endTime);

        List<Question> questions = questionService.findByTestIdWithoutAnswers(testId);

        int duration = (int) Math.min(test.getDuration(), ChronoUnit.MINUTES.between(currentTime, endTime));
        test.setDuration(duration);
        sessionInfo.startSession(test.getId(), currentTime.plusMinutes(duration + INSURANCE_TIME));
        testSessionInfoRepository.save(sessionInfo);

        return new FullTest(test, questions, null, null, null);
    }


    public ResponseEntity endTest(Integer testId, User user, List<Question> answeredQuestions) {
        Test test = testRepository.getTestById(testId);
        TestSessionInfo sessionInfo = user.getTestSessionInfo();
        LocalDateTime currentTime = java.time.LocalDateTime.now();
        LocalDateTime endTime = sessionInfo.getEndTime();

        boolean isTestAllowed = test.getGroup().getId() == user.getGroup().getId();
        if (!sessionInfo.isActive() || !isTestAllowed || !currentTime.isBefore(endTime)) {
            return ResponseEntity.notFound().build();
        }

        List<Question> questions = questionService.findByTestId(testId);

        double testValuation = StreamUtils.zip(answeredQuestions.stream(), questions.stream(), (answered, real) -> {
            if (answered.getCorrectIndexes().size() > real.getCorrectIndexes().size()) {
                return 0.0;
            }

            return answered.getCorrectIndexes().stream().mapToInt(integer -> {
                if (real.getCorrectIndexes().contains(integer)) {
                    return 1;
                }
                return 0;

            }).sum() / (double) real.getNumberOfCorrectAnswers();

        }).mapToDouble(Double::doubleValue).sum();

        Mark mark = new Mark();
        mark.setValue((int) (testValuation * SCALE_OF_MARKS / questions.size()));
        mark.setTest(test);
        mark.setUser(user);
        markService.save(mark);

        sessionInfo.setActive(false);
        testSessionInfoRepository.save(sessionInfo);

        return ResponseEntity.ok(questions);
    }
}
