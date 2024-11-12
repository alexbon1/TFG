package com.syw.APIrest.Stadistics.Repositories;

import com.syw.APIrest.Stadistics.Entitys.HistoricoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface HistoricoRepository extends MongoRepository<HistoricoEntity, String> {
    Optional<HistoricoEntity> findByFechaAndSeccion(String fecha, String seccion);

}