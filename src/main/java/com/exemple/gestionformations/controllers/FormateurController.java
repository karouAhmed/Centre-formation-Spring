package com.exemple.gestionformations.controllers;

import com.exemple.gestionformations.Status;
import com.exemple.gestionformations.entities.Formateur;
import com.exemple.gestionformations.entities.Session;
import com.exemple.gestionformations.repository.FormateurRepository;
import com.exemple.gestionformations.repository.SessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/formateur")
public class FormateurController {
    SessionRepository sessionRepository;
FormateurRepository formateurRepository;
    @GetMapping(value = {"", "session/list"})
    public String getSessions(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<Formateur> formateur = formateurRepository.findByUserEmail(email);
        if (formateur.isPresent()){
            model.addAttribute("sessions", sessionRepository.findAllByFormateurId(formateur.get().getId()));
        }
        return "/formateur/formateur-session";
    }
    @GetMapping(value = {"/session/edit/{id}"})
    public String showUpdateSession(@PathVariable("id") long id, Model model) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + id));
        model.addAttribute("formateurs", formateurRepository.findAll());
        model.addAttribute("session", session);
        return "/formateur/formateur-edit-session";
    }
    @GetMapping(value = {"/session/etudiantList/{id}"})
    public String showSessionEtudians(@PathVariable("id") long id, Model model) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid class	 Id:" + id));
        model.addAttribute("etudiants", session.getEtudiants());
        model.addAttribute("id", session.getId());
        return "/formateur/formateur-etudiant-session";
    }
    @PostMapping(value = {"/session/add"})
    public String addSession(@Validated Session session, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/formateur/formateur-edit-session";
        }
        if (session.getStatus().isEmpty()){
            session.setStatus(Status.Programmé.toString());

        }
        Session sessionTmp = sessionRepository.findById(session.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid class	 Id:" + session.getId()));
        session.setEtudiants(sessionTmp.getEtudiants());
        sessionRepository.save(session);
        return "redirect:/formateur/session/list";
    }
}
