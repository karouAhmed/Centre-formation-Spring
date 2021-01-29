package com.exemple.gestionformations.services;

import com.exemple.gestionformations.entities.Responsable;
import com.exemple.gestionformations.repository.ResponsableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResponsableService {
    @Autowired
    private ResponsableRepository responsableRepository;

    public List<Responsable> getResponsablesList() {
        return responsableRepository.findAll();
    }

    public void saveResponsable(Responsable responsable) {
        responsableRepository.save(responsable);
    }

    public Optional<Responsable> getResponsableById(long id) {
        return responsableRepository.findById(id);
    }

    public void deleteResponsable(Responsable responsable) {
        responsableRepository.delete(responsable);
    }
}
