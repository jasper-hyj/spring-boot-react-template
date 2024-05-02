package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * Controller for react 
 */
@Controller
public class ReactController {

    /**
     * Default react api
     * Index page Mapping
     *
     * @return index page (react build)
     */
    @GetMapping("/")
    public String react() {
        return "index";
    }
}
