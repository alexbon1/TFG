package com.syw.APIrest.Stadistics.Repositories;

import com.syw.APIrest.Stadistics.Entitys.ArmadurasEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArmadurasRepository extends MongoRepository<ArmadurasEntity, String> {

    List<ArmadurasEntity> getByRareza(String rareza);
}