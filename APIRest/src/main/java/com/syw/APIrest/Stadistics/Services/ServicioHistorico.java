package com.syw.APIrest.Stadistics.Services;

import com.syw.APIrest.Stadistics.Entitys.HistoricoEntity;
import com.syw.APIrest.Stadistics.Repositories.HistoricoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioHistorico {
    public static final int MAXIMO = 5;
    @Autowired
    private HistoricoRepository repository;

    public boolean save(HistoricoEntity armadura) {
        try {
            clean();
            repository.save(armadura);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<HistoricoEntity> getFullList() {
        return repository.findAll();
    }

    public Optional<HistoricoEntity> getByID(String id) {
        clean();
        return repository.findById(id);
    }

    public Optional<HistoricoEntity> getByFechaAndSeccion(String fecha, String seccion) {
        clean();
        return repository.findByFechaAndSeccion(fecha, seccion);
    }

    private void clean() {
        List<HistoricoEntity> lista = getFullList();
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaLimite = fechaActual.minusDays(MAXIMO);

        for (HistoricoEntity historico : lista) {
            LocalDate fechaHistorico = LocalDate.parse(historico.getFecha());
            if (fechaHistorico.isBefore(fechaLimite)) {
                repository.delete(historico);
            }
        }

    }
}
