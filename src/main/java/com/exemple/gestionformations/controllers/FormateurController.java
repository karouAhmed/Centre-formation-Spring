package com.exemple.gestionformations.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/formateur")
public class FormateurController {
    @GetMapping(value = {"", "session/list"})
    public String getSessions(Model model) {

        return "/formateur/formateur-session";
    }
}
