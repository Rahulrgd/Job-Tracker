package com.rahul.job_tracker.Home;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomePage {
    
    @GetMapping("/")
    public String welcome(){
        return "Congratulations!!";
    }
}
