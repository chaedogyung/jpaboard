package com.jpaboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController {

    @GetMapping(value = "/")
    public ModelAndView main() {
        ModelAndView mv = new ModelAndView("main");
        return mv;
    }
}
