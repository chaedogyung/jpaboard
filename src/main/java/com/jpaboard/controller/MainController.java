package com.jpaboard.controller;

import com.jpaboard.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final VideoService videoService;

    @GetMapping(value = "/")
    public String main() {
        return "main";
    }
}
