package com.example.nlearn.controllers;

import com.example.nlearn.dtos.FullTestDto;
import com.example.nlearn.dtos.QuestionDto;
import com.example.nlearn.models.CustomOAuth2User;
import com.example.nlearn.records.StudentsContent;
import com.example.nlearn.services.TestPassingService;
import com.example.nlearn.services.TestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("students-home")
public class StudentsHomeController {
    private final TestService testService;
    private final TestPassingService testPassingService;

    public StudentsHomeController(TestService testService, TestPassingService testPassingService) {
        this.testService = testService;
        this.testPassingService = testPassingService;
    }

    @GetMapping("/load-tests")
    public StudentsContent home(@AuthenticationPrincipal CustomOAuth2User user) {
        testPassingService.checkIfAllTestsClosed(user.getDbUser());
        return testService.getContentForStudent(user.getDbUser());
    }

    @GetMapping("/{id}/start")
    public FullTestDto testing(@PathVariable Integer id, @AuthenticationPrincipal CustomOAuth2User user) {
        return testPassingService.startTest(id, user.getDbUser());
    }

    @PostMapping("/{id}/end")
    public ResponseEntity testCheck(@RequestBody List<QuestionDto> questions, @PathVariable Integer id, @AuthenticationPrincipal CustomOAuth2User user) {
        return testPassingService.endTest(id, user.getDbUser(), questions);
    }
}
