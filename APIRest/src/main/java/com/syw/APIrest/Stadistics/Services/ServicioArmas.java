package com.syw.APIrest.Stadistics.Services;

import com.syw.APIrest.Stadistics.Entitys.ArmasEntity;
import com.syw.APIrest.Stadistics.Repositories.ArmasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioArmas {
    @Autowired
    ArmasRepository repository;
    public boolean save(ArmasEntity armadura) {
        try {
            repository.save(armadura);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ArmasEntity> getFullList() {
        return repository.findAll();
    }

    public Optional<ArmasEntity> getByID(String id) {
        return repository.findById(id);
    }

    public boolean delete(String id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ArmasEntity> getByTipo(String Rareza) {
       return repository.getByRareza(Rareza);
    }
}
