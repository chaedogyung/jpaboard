package com.jpaboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value="/thymeleaf")
public class thymeleafExController {

    @GetMapping(value="/ex07")
    public ModelAndView ex07() {
        ModelAndView mav = new ModelAndView("thymeleafEx07");
        return mav;
    }
}
