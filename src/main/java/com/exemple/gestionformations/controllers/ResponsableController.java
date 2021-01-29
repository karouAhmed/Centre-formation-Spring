package com.exemple.gestionformations.controllers;

import com.exemple.gestionformations.Status;
import com.exemple.gestionformations.entities.*;
import com.exemple.gestionformations.repository.ResponsableRepository;
import com.exemple.gestionformations.repository.RoleRepository;
import com.exemple.gestionformations.services.EtudiantService;
import com.exemple.gestionformations.services.FormateurService;
import com.exemple.gestionformations.services.ResponsableService;
import com.exemple.gestionformations.services.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/responsable")
public class ResponsableController {
    EtudiantService etudiantService;
    SessionService sessionService;
    FormateurService formateurService;
    RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping(value = {"", "sessions/list"})
    public String getSessions(Model model) {
        model.addAttribute("sessions", sessionService.getSessionsList());
        return "/responsable/responsable-session";
    }

    @GetMapping(value = {"sessions/signup"})
    public String showAddSession(Model model) {
        model.addAttribute("session", new Session());
        model.addAttribute("formateurs", formateurService.getFormateursList());
        return "/responsable/responsable-ajout-session";
    }

    @PostMapping(value = {"/sessions/add"})
    public String addSession(@Validated Session session, BindingResult result, Model model) {
        System.out.println(session.getFormateur().getUser());
        if (result.hasErrors()) {
            return "/responsable/responsable-ajout-session";
        }

        if (session.getStatus().isEmpty()) {
            session.setStatus(Status.Programmé.toString());

        }
        Optional<Session> sessionTmp = sessionService.getSessiontById(session.getId());
        if (sessionTmp.isPresent()) {
            session.setEtudiants(sessionTmp.get().getEtudiants());
        }
        sessionService.saveSession(session);
        return "redirect:/responsable/sessions/list";
    }

    //**************************FORMATEUR*******************************
    @GetMapping(value = {"/formateurs/list"})
    public String getFormateursAdmin(Model model) {

        model.addAttribute("formateurs", formateurService.getFormateursList());
        return "/responsable/responsable-formateur";
    }

    @GetMapping(value = {"/formateurs/signup"})
    public String showSignupFormateur(Model model) {
        model.addAttribute("formateur", new Formateur());
        return "/responsable/responsable-ajout-formateur";
    }

    @GetMapping(value = {"/formateurs/edit/{id}"})
    public String showUpdateFormateur(@PathVariable("id") long id, Model model) {
        Formateur formateur = formateurService.getFormateurtById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + id));
        model.addAttribute("formateur", formateur);
        return "/responsable/responsable-edit-formateur";
    }

    @PostMapping(value = {"/formateurs/add"})
    public String addFormateur(@Validated Formateur formateur, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/responsable/responsable-ajout-formateur";
        }
        User user = formateur.getUser();
        Role role = roleRepository.findByName("FORMATEUR");
        user.setRoles(Arrays.asList(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        formateur.setUser(user);
        formateurService.saveSFormateur(formateur);
        return "redirect:/responsable/formateurs/list";
    }

    @GetMapping("/formateurs/delete/{id}")
    public String deleteFormateur(@PathVariable("id") long id, Model model) {
        Optional<Formateur> formateur = formateurService.getFormateurtById(id);
        if (formateur.isPresent()) {
            formateurService.deleteFormateur(formateur.get());
            model.addAttribute("formateurs", formateurService.getFormateursList());
            return "redirect:/admin/formateurs/list";
        } else return "redirect:/responsable/formateurs/list";

    }

    //**************************ETUDIANT*******************************
    @GetMapping(value = {"/etudiants/list"})
    public String getEtudiantsAdmin(Model model) {
        model.addAttribute("etudiants", etudiantService.getEtudiantsList());

        return "/responsable/responsable-etudiant";
    }

    @GetMapping(value = {"/etudiant/signup"})
    public String showSignupEtudiant(Model model) {
        model.addAttribute("etudiant", new Etudiant());
        return "/responsable/responsable-ajout-etudiant";
    }

    @PostMapping(value = {"/etudiant/add"})
    public String addEtudiant(@Validated Etudiant etudiant, BindingResult result) {
        if (result.hasErrors()) {
            return "/responsable/responsable-ajout-etudiant";
        }

        User user = etudiant.getUser();
        Role role = roleRepository.findByName("ETUDIANT");
        user.setRoles(Arrays.asList(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        etudiant.setUser(user);
        etudiantService.saveEtudiant(etudiant);
        return "redirect:/responsable/etudiants/list";
    }

    @GetMapping(value = {"/etudiant/edit/{id}"})
    public String showUpdateEtudiant(@PathVariable("id") long id, Model model) {
        Etudiant etudiant = etudiantService.getEtudiantById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + id));
        model.addAttribute("etudiant", etudiant);
        return "/responsable/responsable-edit-etudiant";
    }

    @GetMapping("/etudiant/delete/{id}")
    public String deleteEtudiant(@PathVariable("id") long id, Model model) {
        Optional<Etudiant> etudiant = etudiantService.getEtudiantById(id);
        if (etudiant.isPresent()) {
            etudiantService.deleteEtudiant(etudiant.get());
            model.addAttribute("etudiants", etudiantService.getEtudiantsList());
            return "redirect:/responsable/etudiants/list";
        } else return "redirect:/responsable/etudiants/list";

    }
}
