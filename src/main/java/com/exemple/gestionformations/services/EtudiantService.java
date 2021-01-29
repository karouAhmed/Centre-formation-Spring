package com.exemple.gestionformations.services;

import com.exemple.gestionformations.entities.Etudiant;
import com.exemple.gestionformations.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class EtudiantService {
    @Autowired
    private EtudiantRepository etudiantRepository;

   public Optional<Etudiant> getEtudiantByEmail(String email){
        return etudiantRepository.findByUserEmail(email);
    }
    public Optional<Etudiant> getEtudiantById(long id){
        return etudiantRepository.findById(id);
    }
    public List<Etudiant> getEtudiantsList(){
        return etudiantRepository.findAll();
    }
    public void deleteEtudiant(Etudiant etudiant){
         etudiantRepository.delete(etudiant);
    }
    public void saveEtudiant(Etudiant etudiant){
        etudiantRepository.save(etudiant);
    }
}
