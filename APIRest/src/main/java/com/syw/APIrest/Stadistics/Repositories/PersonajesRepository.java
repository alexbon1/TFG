package com.syw.APIrest.Stadistics.Repositories;

import com.syw.APIrest.Stadistics.Entitys.PersonajesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface PersonajesRepository extends MongoRepository<PersonajesEntity, String> {
    Optional<PersonajesEntity> findByImagen(String imagen);
}

