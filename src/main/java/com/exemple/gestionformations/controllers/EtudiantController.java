package com.exemple.gestionformations.controllers;

import com.exemple.gestionformations.entities.Etudiant;
import com.exemple.gestionformations.entities.Session;
import com.exemple.gestionformations.services.EtudiantService;
import com.exemple.gestionformations.services.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/etudiant")
public class EtudiantController {
    SessionService sessionService;
    EtudiantService etudiantService;

    @GetMapping(value = {"", "session/list"})
    public String getSessions(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
          /*  Optional<Formateur> formateur = formateurRepository.findByUserEmail(email);
            if (formateur.isPresent()){
                model.addAttribute("sessions", sessionRepository.findAllByFormateurId(formateur.get().getId()));
            }else{
                model.addAttribute("sessions", null);
            }
           */
        Etudiant etudiant = etudiantService.getEtudiantByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid class	 email:" + email));
        model.addAttribute("sessions", etudiant.getSessions());
        return "/etudiant/etudiant-session";
    }
    @GetMapping("session/cancel/{id}")
    public String cancelSession(@PathVariable("id") long id, Model modell) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Etudiant etudiant = etudiantService.getEtudiantByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + email));
        etudiant.getSessions().removeIf(session -> session.getId() == id);
        etudiantService.saveEtudiant(etudiant);
        return "redirect:/etudiant";
    }
    @GetMapping(value = {"formation/list"})
    public String getFormartions(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Etudiant etudiant = etudiantService.getEtudiantByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + email));
        model.addAttribute("id", etudiant.getId());
        List<Session> sessions = sessionService.getActiveSessionsList();
        sessions.removeIf(e -> e.getEtudiants().contains(etudiant));
        model.addAttribute("sessions", sessions);
        return "/etudiant/etudiant-formation";
    }
    @GetMapping("formation/suivie/{id}")
    public String suivieSession(@PathVariable("id") long id, Model modell) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Etudiant etudiant = etudiantService.getEtudiantByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + email));
        Session session = sessionService.getSessiontById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + id));
       session.getEtudiants().add(etudiant);
        sessionService.saveSession(session);
        return "redirect:/etudiant/formation/list";
    }

}
