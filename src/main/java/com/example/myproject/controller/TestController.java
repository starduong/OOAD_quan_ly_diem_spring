package com.example.myproject.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Test Controller - CHỈ ACTIVE TRONG DEV MODE
 * Dùng để test các endpoint mà không cần authentication
 */
@Controller
@Profile("dev")
public class TestController {
    
    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "<h1>✅ DEV MODE ACTIVE - Authentication Bypassed!</h1>" +
               "<p>App is running in development mode.</p>" +
               "<ul>" +
               "<li><a href='/admin/'>Admin Area (normally requires ADMIN role)</a></li>" +
               "<li><a href='/client/sv/'>Student Area (normally requires SINH_VIEN role)</a></li>" +
               "<li><a href='/client/gv/'>Teacher Area (normally requires GIANG_VIEN role)</a></li>" +
               "</ul>";
    }
    
    @GetMapping("/admin")
    @ResponseBody
    public String admin() {
        return "<h1>✅ Admin Area</h1><p>You accessed /admin/ without authentication!</p>" +
               "<p><a href='/'>Back to home</a></p>";
    }
    
    @GetMapping("/admin/")
    @ResponseBody
    public String adminSlash() {
        return admin();
    }
    
    @GetMapping("/client/sv")
    @ResponseBody
    public String student() {
        return "<h1>✅ Student Area</h1><p>You accessed /client/sv/ without authentication!</p>" +
               "<p><a href='/'>Back to home</a></p>";
    }
    
    @GetMapping("/client/sv/")
    @ResponseBody
    public String studentSlash() {
        return student();
    }
    
    @GetMapping("/client/gv")
    @ResponseBody
    public String teacher() {
        return "<h1>✅ Teacher Area</h1><p>You accessed /client/gv/ without authentication!</p>" +
               "<p><a href='/'>Back to home</a></p>";
    }
    
    @GetMapping("/client/gv/")
    @ResponseBody
    public String teacherSlash() {
        return teacher();
    }
}
