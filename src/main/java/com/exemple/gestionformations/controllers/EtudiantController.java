package com.exemple.gestionformations.controllers;

import com.exemple.gestionformations.entities.Etudiant;
import com.exemple.gestionformations.entities.Formateur;
import com.exemple.gestionformations.entities.Session;
import com.exemple.gestionformations.repository.EtudiantRepository;
import com.exemple.gestionformations.repository.FormateurRepository;
import com.exemple.gestionformations.repository.SessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/etudiant")
public class EtudiantController {
    SessionRepository sessionRepository;
    FormateurRepository formateurRepository;
EtudiantRepository etudiantRepository;
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
           Etudiant etudiant = etudiantRepository.findByUserEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid class	 email:" + email));
            model.addAttribute("sessions", etudiant.getSessions());
            return "/etudiant/etudiant-session";
        }
    @GetMapping(value = { "formation/list"})
    public String getFormartions(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Etudiant etudiant = etudiantRepository.findByUserEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + email));
        model.addAttribute("id", etudiant.getId());
        List<Session> sessions = sessionRepository.findAllByStatusIsNotLike("Terminé");
        sessions.removeIf(e->e.getEtudiants().contains(etudiant));
        model.addAttribute("sessions", sessions);
        return "/etudiant/etudiant-formation";
    }

}
