package com.exemple.gestionformations.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/etudiant")
public class EtudiantController {
    @GetMapping(value = {"", "etudiant/list"})
    public String getForamtions(Model model) {

        return "/etudiant/etudiant-formation";
    }
}
