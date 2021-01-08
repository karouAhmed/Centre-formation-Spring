package com.exemple.gestionformations.controllers;

import com.exemple.gestionformations.Status;
import com.exemple.gestionformations.entities.*;
import com.exemple.gestionformations.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

    SessionRepository sessionRepository;
    FormateurRepository formateurRepository;
    RoleRepository roleRepository;
    EtudiantRepository etudiantRepository;
    ResponsableRepository responsableRepository;
    private BCryptPasswordEncoder passwordEncoder;
    //**************************SESSION*******************************

    @GetMapping(value = {"", "sessions/list"})
    public String getSessionsAdmin(Model model) {
        model.addAttribute("sessions", sessionRepository.findAll());
        return "/admin/admin-session";
    }
    @GetMapping(value = {"sessions/signup"})
    public String showAddSession(Model model) {
        model.addAttribute("session", new Session());
        model.addAttribute("formateurs", formateurRepository.findAll());
        return "/admin/admin-ajout-session";
    }
    @PostMapping(value = {"/sessions/add"})
    public String addSession(@Validated Session session, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/admin/admin-ajout-session";
        }
if (session.getStatus().isEmpty()){
            session.setStatus(Status.Programmé.toString());

        }
        sessionRepository.save(session);
        return "redirect:/admin/sessions/list";
    }
    @GetMapping(value = {"/session/etudiantList/{id}"})
    public String showSessionEtudians(@PathVariable("id") long id, Model model) {
      Session session = sessionRepository.findById(id)
              .orElseThrow(() -> new IllegalArgumentException("Invalid class	 Id:" + id));
        model.addAttribute("etudiants", session.getEtudiants());
        model.addAttribute("id", session.getId());
        return "/admin/admin-etudiant-session";
    }
    @GetMapping(value = {"/session/edit/{id}"})
    public String showUpdateSession(@PathVariable("id") long id, Model model) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + id));
        model.addAttribute("formateurs", formateurRepository.findAll());
        model.addAttribute("session", session);
        return "/admin/admin-edit-session";
    }
    @GetMapping("/session/delete/{id}")
    public String deleteSession(@PathVariable("id") long id, Model model) {
        Optional<Session> session = sessionRepository.findById(id);
        if (session.isPresent())
        {
            sessionRepository.delete(session.get());
            model.addAttribute("session", sessionRepository.findAll());
            return "redirect:/admin/sessions/list";
        }
        else return "redirect:/admin/sessions/list" ;

    }
    //**************************FORMATEUR*******************************
    @GetMapping(value = {"/formateurs/list"})
    public String getFormateursAdmin(Model model) {

        model.addAttribute("formateurs", formateurRepository.findAll());
        return "/admin/admin-formateur";
    }

    @GetMapping(value = {"/formateurs/signup"})
    public String showSignupFormateur(Model model) {
        model.addAttribute("formateur", new Formateur());
        return "/admin/admin-ajout-formateur";
    }

    @GetMapping(value = {"/formateurs/edit/{id}"})
    public String showUpdateFormateur(@PathVariable("id") long id, Model model) {
        Formateur formateur = formateurRepository.findById(id)
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
        formateurRepository.save(formateur);
        return "redirect:/admin/formateurs/list";
    }
    @GetMapping("/formateurs/delete/{id}")
    public String deleteFormateur(@PathVariable("id") long id, Model model) {
        Optional<Formateur> formateur = formateurRepository.findById(id);
        if (formateur.isPresent())
        {
            formateurRepository.delete(formateur.get());
            model.addAttribute("formateurs", formateurRepository.findAll());
            return "redirect:/admin/formateurs/list";
        }
        else return "redirect:/admin/formateurs/list" ;

    }
//**************************ETUDIANT*******************************
    @GetMapping(value = {"/etudiants/list"})
    public String getEtudiantsAdmin(Model model) {
        model.addAttribute("etudiants", etudiantRepository.findAll());

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
        etudiantRepository.save(etudiant);
        return "redirect:/admin/etudiants/list";
    }
    @GetMapping(value = {"/etudiant/edit/{id}"})
    public String showUpdateEtudiant(@PathVariable("id") long id, Model model) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + id));
        model.addAttribute("etudiant", etudiant);
        return "/admin/admin-edit-etudiant";
    }
    @GetMapping("/etudiant/delete/{id}")
    public String deleteEtudiant(@PathVariable("id") long id, Model model) {
        Optional<Etudiant> etudiant = etudiantRepository.findById(id);
        if (etudiant.isPresent())
        {
            etudiantRepository.delete(etudiant.get());
            model.addAttribute("etudiants", etudiantRepository.findAll());
            return "redirect:/admin/etudiants/list";
        }
        else return "redirect:/admin/etudiants/list" ;

    }
    @GetMapping("/etudiant/affecter/{id}")
    public String affecterEtudiant(@PathVariable("id") long id, Model model) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + id));
        model.addAttribute("id", id);
        List<Session> sessions = sessionRepository.findAllByStatusIsNotLike("Terminé");
sessions.removeIf(e->e.getEtudiants().contains(etudiant));
        model.addAttribute("sessions", sessions);
        return "/admin/admin-affecter-etudiant";

    }
    @RequestMapping(value = {"/etudiant/affecterSession/{idSession}/{idEtudiant}"})
    public String addEtudiantToSession(@PathVariable("idSession") long idSession,@PathVariable("idEtudiant") long idEtudiant) {

        Etudiant etudiant = etudiantRepository.findById(idEtudiant)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + idEtudiant));
        Session session = sessionRepository.findById(idSession)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + idSession));

       session.getEtudiants().add(etudiant);
       sessionRepository.save(session);

        return "redirect:/admin/etudiants/list";
    }
    @RequestMapping(value = {"/etudiant/deleteFromSession/{idSession}/{idEtudiant}"})
    public String deleteEtudiantfFromSession(@PathVariable("idSession") long idSession,@PathVariable("idEtudiant") long idEtudiant) {


        Session session = sessionRepository.findById(idSession)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + idSession));

        session.getEtudiants().removeIf(e->e.getId()==idEtudiant);
        sessionRepository.save(session);

        return "redirect:/admin/sessions/list";
    }
//**************************RESPONSATBLE*******************************

    @GetMapping(value = {"/responsables/list"})
    public String getAdministrateursAdmin(Model model) {
        model.addAttribute("responsables", responsableRepository.findAll());
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
        responsableRepository.save(responsable);
        return "redirect:/admin/responsables/list";
    }
    @GetMapping(value = {"/responsable/edit/{id}"})
    public String showUpdateResponsable(@PathVariable("id") long id, Model model) {
        Responsable responsable = responsableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject Id:" + id));
        model.addAttribute("responsable", responsable);
        return "/admin/admin-edit-responsable";
    }
    @GetMapping("/responsable/delete/{id}")
    public String deleteResponsable(@PathVariable("id") long id, Model model) {
        Optional<Responsable> responsable = responsableRepository.findById(id);
        if (responsable.isPresent())
        {
            responsableRepository.delete(responsable.get());
            model.addAttribute("responsables", responsableRepository.findAll());
            return "redirect:/admin/responsables/list";
        }
        else return "redirect:/admin/responsables/list" ;

    }
}
