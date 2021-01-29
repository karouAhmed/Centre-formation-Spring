package com.exemple.gestionformations.services;

import com.exemple.gestionformations.Status;
import com.exemple.gestionformations.entities.Etudiant;
import com.exemple.gestionformations.entities.Session;
import com.exemple.gestionformations.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionService {
    @Autowired
    private SessionRepository  sessionRepository;
    public List<Session> getSessionsList(){
        return sessionRepository.findAll();
    }
    public List<Session> getActiveSessionsList(){
        return sessionRepository.findAllByStatusIsNotLike(Status.Terminé.toString());
    }
    public void saveSession(Session session){
        sessionRepository.save(session);
    }
    public Optional<Session> getSessiontById(long id){
        return sessionRepository.findById(id);
    }
    public void deleteService(Session session){
        sessionRepository.delete(session);
    }
   public List<Session> getSessionsByFormateur(Long id){
        return sessionRepository.findAllByFormateurId(id);
   }

}
