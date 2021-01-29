package com.exemple.gestionformations.controllers;

import com.exemple.gestionformations.Status;
import com.exemple.gestionformations.entities.*;
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
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {
    EtudiantService etudiantService;
    SessionService sessionService;
    FormateurService formateurService;
    RoleRepository roleRepository;
    ResponsableService responsableService;
    private BCryptPasswordEncoder passwordEncoder;
    //**************************SESSION*******************************

    @GetMapping(value = {"", "sessions/list"})
    public String getSessionsAdmin(Model model) {
        model.addAttribute("sessions", sessionService.getSessionsList());
        return "/admin/admin-session";
    }

    @GetMapping(value = {"sessions/signup"})
    public String showAddSession(Model model) {
        model.addAttribute("session", new Session());
        model.addAttribute("formateurs", formateurService.getFormateursList());
        return "/admin/admin-ajout-session";
    }

    @PostMapping(value = {"/sessions/add"})
    public String addSession(@Validated Session session, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/admin/admin-ajout-session";
        }
        if (session.getStatus().isEmpty()) {
            session.setStatus(Status.Programmé.toString());
        }
        Optional<Session> sessionTmp = sessionService.getSessiontById(session.getId());
        if (sessionTmp.isPresent()) {
            session.setEtudiants(sessionTmp.get().getEtudiants());
        }

        sessionService.saveSession(session);
        return "redirect:/admin/sessions/list";
    }

    @GetMapping(value = {"/session/etudiantList/{id}"})
    public String showSessionEtudians(@PathVariable("id") long id, Model model) {
        Session session = sessionService.getSessiontById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid class	 Id:" + id));
        model.addAttribute("etudiants", session.getEtudiants());
        model.addAttribute("id", session.getId());
        return "/admin/admin-etudiant-session";
    }

    @GetMapping(value = {"/session/edit/{id}"})
    public String showUpdateSession(@PathVariable("id") long id, Model model) {
        Session session = sessionService.getSessiontById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + id));
        model.addAttribute("formateurs", formateurService.getFormateursList());
        model.addAttribute("session", session);


        return "/admin/admin-edit-session";
    }

    @GetMapping("/session/delete/{id}")
    public String deleteSession(@PathVariable("id") long id, Model model) {
        Optional<Session> session = sessionService.getSessiontById(id);
        if (session.isPresent()) {
            sessionService.deleteService(session.get());
            model.addAttribute("session", sessionService.getSessionsList());
            return "redirect:/admin/sessions/list";
        } else return "redirect:/admin/sessions/list";

    }

    //**************************FORMATEUR*******************************
    @GetMapping(value = {"/formateurs/list"})
    public String getFormateursAdmin(Model model) {

        model.addAttribute("formateurs", formateurService.getFormateursList());
        return "/admin/admin-formateur";
    }

    @GetMapping(value = {"/formateurs/signup"})
    public String showSignupFormateur(Model model) {
        model.addAttribute("formateur", new Formateur());
        return "/admin/admin-ajout-formateur";
    }

    @GetMapping(value = {"/formateurs/edit/{id}"})
    public String showUpdateFormateur(@PathVariable("id") long id, Model model) {
        Formateur formateur = formateurService.getFormateurtById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + id));
        model.addAttribute("formateur", formateur);
        return "/admin/admin-edit-formateur";
    }

    @PostMapping(value = {"/formateurs/add"})
    public String addFormateur(@Validated Formateur formateur, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/admin/admin-ajout-formateur";
        }
        User user = formateur.getUser();
        Role role = roleRepository.findByName("FORMATEUR");
        user.setRoles(Arrays.asList(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        formateur.setUser(user);
        formateurService.saveSFormateur(formateur);
        return "redirect:/admin/formateurs/list";
    }

    @GetMapping("/formateurs/delete/{id}")
    public String deleteFormateur(@PathVariable("id") long id, Model model) {
        Optional<Formateur> formateur = formateurService.getFormateurtById(id);
        if (formateur.isPresent()) {
            formateurService.deleteFormateur(formateur.get());
            model.addAttribute("formateurs", formateurService.getFormateursList());
            return "redirect:/admin/formateurs/list";
        } else return "redirect:/admin/formateurs/list";

    }

    //**************************ETUDIANT*******************************
    @GetMapping(value = {"/etudiants/list"})
    public String getEtudiantsAdmin(Model model) {
        model.addAttribute("etudiants", etudiantService.getEtudiantsList());

        return "/admin/admin-etudiant";
    }

    @GetMapping(value = {"/etudiant/signup"})
    public String showSignupEtudiant(Model model) {
        model.addAttribute("etudiant", new Etudiant());
        return "/admin/admin-ajout-etudiant";
    }

    @PostMapping(value = {"/etudiant/add"})
    public String addEtudiant(@Validated Etudiant etudiant, BindingResult result) {
        if (result.hasErrors()) {
            return "/admin/admin-ajout-etudiant";
        }

        User user = etudiant.getUser();
        Role role = roleRepository.findByName("ETUDIANT");
        user.setRoles(Arrays.asList(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        etudiant.setUser(user);
        etudiantService.saveEtudiant(etudiant);
        return "redirect:/admin/etudiants/list";
    }

    @GetMapping(value = {"/etudiant/edit/{id}"})
    public String showUpdateEtudiant(@PathVariable("id") long id, Model model) {
        Etudiant etudiant = etudiantService.getEtudiantById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + id));
        model.addAttribute("etudiant", etudiant);
        return "/admin/admin-edit-etudiant";
    }

    @GetMapping("/etudiant/delete/{id}")
    public String deleteEtudiant(@PathVariable("id") long id, Model model) {
        Optional<Etudiant> etudiant = etudiantService.getEtudiantById(id);
        if (etudiant.isPresent()) {
            etudiantService.deleteEtudiant(etudiant.get());
            model.addAttribute("etudiants", etudiantService.getEtudiantsList());
            return "redirect:/admin/etudiants/list";
        } else return "redirect:/admin/etudiants/list";

    }

    @GetMapping("/etudiant/affecter/{id}")
    public String affecterEtudiant(@PathVariable("id") long id, Model model) {
        Etudiant etudiant = etudiantService.getEtudiantById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + id));
        model.addAttribute("id", id);
        List<Session> sessions = sessionService.getActiveSessionsList();
        sessions.removeIf(e -> e.getEtudiants().contains(etudiant));
        model.addAttribute("sessions", sessions);
        return "/admin/admin-affecter-etudiant";

    }

    @RequestMapping(value = {"/etudiant/affecterSession/{idSession}/{idEtudiant}"})
    public String addEtudiantToSession(@PathVariable("idSession") long idSession, @PathVariable("idEtudiant") long idEtudiant) {

        Etudiant etudiant = etudiantService.getEtudiantById(idEtudiant)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + idEtudiant));
        Session session = sessionService.getSessiontById(idSession)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + idSession));

        session.getEtudiants().add(etudiant);
        sessionService.saveSession(session);

        return "redirect:/admin/etudiants/list";
    }

    @RequestMapping(value = {"/etudiant/deleteFromSession/{idSession}/{idEtudiant}"})
    public String deleteEtudiantfFromSession(@PathVariable("idSession") long idSession, @PathVariable("idEtudiant") long idEtudiant) {


        Session session = sessionService.getSessiontById(idSession)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + idSession));

        session.getEtudiants().removeIf(e -> e.getId() == idEtudiant);
        sessionService.saveSession(session);

        return "redirect:/admin/sessions/list";
    }
//**************************RESPONSATBLE*******************************

    @GetMapping(value = {"/responsables/list"})
    public String getAdministrateursAdmin(Model model) {
        model.addAttribute("responsables", responsableService.getResponsablesList());
        return "/admin/admin-responsable";
    }

    @GetMapping(value = {"/responsable/signup"})
    public String showSignupResponsable(Model model) {
        model.addAttribute("responsable", new Responsable());
        return "/admin/admin-ajout-responsable";
    }

    @PostMapping(value = {"/responsable/add"})
    public String addResponsable(@Validated Responsable responsable, BindingResult result) {
        if (result.hasErrors()) {
            return "/admin/admin-ajout-responsable";
        }

        User user = responsable.getUser();
        Role role = roleRepository.findByName("RESPONSABLE");
        user.setRoles(Arrays.asList(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        responsable.setUser(user);
        responsableService.saveResponsable(responsable);
        return "redirect:/admin/responsables/list";
    }

    @GetMapping(value = {"/responsable/edit/{id}"})
    public String showUpdateResponsable(@PathVariable("id") long id, Model model) {
        Responsable responsable = responsableService.getResponsableById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + id));
        model.addAttribute("responsable", responsable);
        return "/admin/admin-edit-responsable";
    }

    @GetMapping("/responsable/delete/{id}")
    public String deleteResponsable(@PathVariable("id") long id, Model model) {
        Optional<Responsable> responsable = responsableService.getResponsableById(id);
        if (responsable.isPresent()) {
            responsableService.deleteResponsable(responsable.get());
            model.addAttribute("responsables", responsableService.getResponsablesList());
            return "redirect:/admin/responsables/list";
        } else return "redirect:/admin/responsables/list";

    }
}
