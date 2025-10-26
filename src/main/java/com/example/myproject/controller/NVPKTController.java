package com.example.myproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/nvpkt")
public class NVPKTController {
    @GetMapping("/index") 
    public String nvpktIndex(){
        return "nvpkt/index"; 
    }
}
