package com.example.kutuphaneotomasyon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnaSayfaController {

    @GetMapping("/")
    public String anaSayfa() {
        return "redirect:/index.html";
    }
}
