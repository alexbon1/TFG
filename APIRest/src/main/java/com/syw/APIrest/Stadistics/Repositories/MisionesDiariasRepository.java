package com.syw.APIrest.Stadistics.Repositories;

import com.syw.APIrest.Stadistics.Entitys.ArmadurasEntity;
import com.syw.APIrest.Stadistics.Entitys.MisionesDiariasEntity;
import com.syw.APIrest.Stadistics.Entitys.PersonajesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MisionesDiariasRepository extends MongoRepository<MisionesDiariasEntity, String> {
    List<MisionesDiariasEntity> findBytipoUser(String tipo);

}