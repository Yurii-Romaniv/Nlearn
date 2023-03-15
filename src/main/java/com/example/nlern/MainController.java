package com.example.nlern;


import com.example.nlern.models.*;
import com.example.nlern.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
@RequestMapping("/home")
public class MainController {
    @Autowired
    private UserRepository UsRepo;
    @Autowired
    private GroupRepository GroupRepo;



    @GetMapping("")
    public String homePage(Model model){
        List<User> users= UsRepo.findTop5ByOrderByIdDesc();
        model.addAttribute("users", users);
        return "teachers_home";
    }


    @GetMapping("/create_user")
    public String createUserPage(@ModelAttribute("user") User user){

        return "create_user";
    }


    @PostMapping("/create_user")
    public String createUser(@ModelAttribute("user") User user){
        user.setRole("student");
        UsRepo.save(user);



        return "create_user";
    }



}

/*
    @GetMapping("/home/testing")
    public String testingPage(){
        return "testing";
    }



    @GetMapping("/home/create_test")
    public String createTestPage(){
        return "createTest";
    }

    @GetMapping("/home/redact_test")
    public String redactTestPage(){
        return "redactTest";
    }

    @GetMapping("/home/create_group")
    public String createGroupPage(){
        return "createGroup";
    }

    @GetMapping("/home/redact_group")
    public String redactGroupPage(){
        return "redactGroup";
    }


    @GetMapping("/home/create_user")
    public String createUserPage(){
        return "createUser";
    }

    @GetMapping("/home/redact_user")
    public String redactUserPage(){
        return "redactUser";
    }
*/