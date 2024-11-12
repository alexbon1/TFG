package com.syw.APIrest.Stadistics.Services;

import com.syw.APIrest.Stadistics.Repositories.PersonajesRepository;
import com.syw.APIrest.Stadistics.Entitys.PersonajesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioPersonajes {

    private PersonajesRepository repository;

    @Autowired
    public ServicioPersonajes(PersonajesRepository repository) {
        this.repository = repository;
    }

    public PersonajesEntity save(PersonajesEntity personaje) {
        return repository.save(personaje);
    }

    public PersonajesEntity modify(String id, String campo, String valor) {
        Optional<PersonajesEntity> optionalPersonaje = repository.findById(id);
        return optionalPersonaje.map(personaje -> {
            switch (campo) {
                case "historia":
                    personaje.setHistoria(valor);
                    break;
                case "nombre":
                    personaje.setNombre(valor);
                    break;
                case "imagen":
                    personaje.setImagen(valor);
                    break;
                default:
                    throw new IllegalArgumentException("Campo no válido");
            }
            return repository.save(personaje);
        }).orElse(null); // Devuelve null si no se encuentra el ID del personaje
    }

    public List<PersonajesEntity> getFullList() {
        return repository.findAll();
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public PersonajesEntity getById(String id) {
        Optional<PersonajesEntity> optionalPersonaje = repository.findById(id);
        return optionalPersonaje.orElse(null); // Devuelve null si no se encuentra el ID del personaje
    }

    public PersonajesEntity getByImagen(String imagen) {
        Optional<PersonajesEntity> optionalPersonaje = repository.findByImagen(imagen);
        return optionalPersonaje.orElse(null); // Devuelve null si no se encuentra la imagen del personaje
    }
}
