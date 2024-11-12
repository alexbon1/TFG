package com.syw.APIrest.Stadistics.Services;

import com.syw.APIrest.Stadistics.Entitys.MisionesDiariasEntity;
import com.syw.APIrest.Stadistics.Repositories.MisionesDiariasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioMisionesDiarias {
    @Autowired
    private MisionesDiariasRepository repository;

    public boolean save(MisionesDiariasEntity armadura) {
        try {
            repository.save(armadura);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<MisionesDiariasEntity> getFullList() {
        return repository.findAll();
    }

    public Optional<MisionesDiariasEntity> getByID(String id) {
        return repository.findById(id);
    }
    public List<MisionesDiariasEntity> getByTipoUser(String id) {
        List<MisionesDiariasEntity> misiones = repository.findBytipoUser(id);
        return misiones;
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
}