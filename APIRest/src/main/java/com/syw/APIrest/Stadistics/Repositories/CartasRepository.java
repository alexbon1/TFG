package com.syw.APIrest.Stadistics.Repositories;

import com.syw.APIrest.Stadistics.Entitys.ArmasEntity;
import com.syw.APIrest.Stadistics.Entitys.CartasEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartasRepository extends MongoRepository<CartasEntity, String> {
}
