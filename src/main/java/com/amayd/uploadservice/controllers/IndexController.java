package com.amayd.uploadservice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.web.bind.annotation.RequestMethod.*;


@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping(method = GET)
    public String indexPage(){
        System.out.println("printing from Index controller");
        return "redirect:/upload";
    }
}
