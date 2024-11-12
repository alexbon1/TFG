package com.syw.APIrest.Stadistics.Services;

import com.syw.APIrest.Stadistics.Entitys.ArmadurasEntity;
import com.syw.APIrest.Stadistics.Repositories.ArmadurasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioArmaduras {
    @Autowired
    private ArmadurasRepository repository;

    // Método para crear una nueva armadura
    public boolean save(ArmadurasEntity armadura) {
        try {
            repository.save(armadura);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para obtener todas las armaduras
    public List<ArmadurasEntity> getFullList() {
        return repository.findAll();
    }

    // Método para obtener una armadura por su ID
    public Optional<ArmadurasEntity> getByID(String id) {
        return repository.findById(id);
    }

    // Método para eliminar una armadura por su ID
    public boolean delete(String id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ArmadurasEntity> getByTipo(String rareza) {
        return repository.getByRareza(rareza);
    }
}