package com.exemple.gestionformations.services;

import com.exemple.gestionformations.Status;
import com.exemple.gestionformations.entities.Formateur;
import com.exemple.gestionformations.entities.Session;
import com.exemple.gestionformations.repository.FormateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FormateurService {
    @Autowired
    private FormateurRepository formateurRepository;
    public Optional<Formateur> getFormateurByEmail(String email){
        return formateurRepository.findByUserEmail(email);
    }
    public List<Formateur> getFormateursList(){
        return formateurRepository.findAll();
    }
    public void saveSFormateur(Formateur formateur){
        formateurRepository.save(formateur);
    }
    public Optional<Formateur> getFormateurtById(long id){
        return formateurRepository.findById(id);
    }
    public void deleteFormateur(Formateur formateur){
        formateurRepository.delete(formateur);
    }

}
