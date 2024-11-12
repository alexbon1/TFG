package com.syw.APIrest.Stadistics.Repositories;

import com.syw.APIrest.Stadistics.Entitys.ArmasEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArmasRepository extends MongoRepository<ArmasEntity, String> {

    List<ArmasEntity> getByRareza(String rareza);
}