package com.example.nlearn.services;

import com.example.nlearn.exception.TestNotAllowedException;
import com.example.nlearn.dtos.AnswerDto;
import com.example.nlearn.dtos.FullTestDto;
import com.example.nlearn.dtos.QuestionDto;
import com.example.nlearn.models.AnswerVariant;
import com.example.nlearn.models.Mark;
import com.example.nlearn.models.MarkDetail;
import com.example.nlearn.models.Question;
import com.example.nlearn.models.Test;
import com.example.nlearn.models.TestSessionInfo;
import com.example.nlearn.models.User;
import com.example.nlearn.repos.TestSessionInfoRepository;
import jakarta.transaction.Transactional;
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
    private final TestService testService;
    private final UserService userService;
    private final MarkService markService;
    private final ModelMapper modelMapper;
    TestSessionInfoRepository testSessionInfoRepository;

    public TestPassingService(TestSessionInfoRepository testSessionInfoRepository, TestService testService, UserService userService, MarkService markService, ModelMapper modelMapper) {
        this.testSessionInfoRepository = testSessionInfoRepository;
        this.testService = testService;
        this.userService = userService;

        this.markService = markService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void raiseErrorIfTestingNotAllowed(Test test, int testId, User user, TestSessionInfo sessionInfo, LocalDateTime currentTime, LocalDateTime endTime) {
        boolean isTestForThisStudent = user.getGroups().contains(test.getGroup());
        boolean areRetriesOver = markService.countMarksByUserAndTest(user, test) >= test.getNumberOfRetries();
        if ((sessionInfo.isActive() && sessionInfo.getCurrentTest().getId() != testId) || !isTestForThisStudent || endTime.isBefore(currentTime) || areRetriesOver) {
            throw new TestNotAllowedException();
        }
    }

    @Transactional
    public FullTestDto startTest(Integer testId, User user) {
        user = userService.getUser(user.getId());
        Test test = testService.getTestById(testId);
        FullTestDto fullTestDto = modelMapper.map(test, FullTestDto.class);
        TestSessionInfo sessionInfo = testSessionInfoRepository.getById(user.getTestSessionInfo().getId());

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime endTime = fullTestDto.getEndTime();

        raiseErrorIfTestingNotAllowed(test, testId, user, sessionInfo, currentTime, endTime);

        fullTestDto.getQuestions().forEach(question -> {
            question.setMultivariate(question.getAnswerVariants().stream()
                    .filter(AnswerDto::isCorrect)
                    .count() > 1
            );
            question.getAnswerVariants().forEach(a -> a.setCorrect(false));

        });

        if (sessionInfo.isActive()) {
            fullTestDto.setEndTime(sessionInfo.getEndTime().minusMinutes(INSURANCE_TIME));
        } else {
            int duration = (int) Math.min(fullTestDto.getDuration(), ChronoUnit.MINUTES.between(currentTime, endTime));
            LocalDateTime sessionEndTime = currentTime.plusMinutes(duration);
            fullTestDto.setEndTime(sessionEndTime);
            sessionInfo.startSession(test, sessionEndTime.plusMinutes(INSURANCE_TIME));
            testSessionInfoRepository.save(sessionInfo);
        }

        return fullTestDto;
    }

    @Transactional
    public ResponseEntity endTest(Integer testId, User user, List<QuestionDto> answeredQuestions) {
        Test test = testService.getTestById(testId);
        TestSessionInfo sessionInfo = testSessionInfoRepository.getById(user.getTestSessionInfo().getId());
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime endTime = sessionInfo.getEndTime();

        boolean isTestAllowed = test.getGroup().getUsers().stream().anyMatch(u -> u.getId() == user.getId());
        if (!sessionInfo.isActive() || !isTestAllowed || !currentTime.isBefore(endTime)) {
            return ResponseEntity.notFound().build();
        }

        List<Question> questions = test.getQuestions();

        int testNegativeValuation = StreamUtils.zip(answeredQuestions.stream(), questions.stream(), (answered, real) ->
                StreamUtils.zip(answered.getAnswerVariants().stream(), real.getAnswerVariants().stream(), (answeredVariant, realVariant) ->
                        answeredVariant.isCorrect() == realVariant.isCorrect() ? 0 : -1
                ).mapToInt(Integer::intValue).sum()
        ).mapToInt(Integer::intValue).sum();

        int correctAnswerAmount =
                questions.stream().mapToInt(q ->
                        Math.toIntExact(q.getAnswerVariants().stream()
                                .filter(AnswerVariant::isCorrect)
                                .count())
                ).sum();

        List<MarkDetail> markDetails = answeredQuestions.stream().map(q ->
                q.getAnswerVariants().stream()
                        .filter(AnswerDto::isCorrect)
                        .map(a -> MarkDetail.builder().selectedAnswerVariant(modelMapper.map(a, AnswerVariant.class)).build())
                        .toList()
        ).flatMap(List::stream).toList();

        Mark mark = new Mark();
        double testValuation = (double) (correctAnswerAmount + testNegativeValuation) / correctAnswerAmount * SCALE_OF_MARKS;
        mark.setValue((int) (testValuation > 0 ? testValuation : 0));
        mark.setTest(test);
        mark.setUser(user);
        mark.setDetails(markDetails);
        markService.save(mark);

        sessionInfo.setActive(false);
        testSessionInfoRepository.save(sessionInfo);

        return ResponseEntity.ok(mark.getValue());
    }

    @Transactional
    public void checkIfAllTestsClosed(User user) {
        TestSessionInfo sessionInfo = testSessionInfoRepository.getById(user.getTestSessionInfo().getId());
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime endTime = sessionInfo.getEndTime();

        if (sessionInfo.isActive() && currentTime.isAfter(endTime)) {
            Mark mark = new Mark();
            mark.setValue(0);
            mark.setTest(sessionInfo.getCurrentTest());
            mark.setUser(user);
            markService.save(mark);

            sessionInfo.setActive(false);
            testSessionInfoRepository.save(sessionInfo);
        }
    }
}
