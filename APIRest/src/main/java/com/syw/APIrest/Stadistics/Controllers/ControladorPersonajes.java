package com.syw.APIrest.Stadistics.Controllers;

import com.syw.APIrest.Stadistics.Entitys.PersonajesEntity;
import com.syw.APIrest.Stadistics.Services.ServicioPersonajes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Personajes")
public class ControladorPersonajes {

    private final ServicioPersonajes servicioPersonajes;

    @Autowired
    public ControladorPersonajes(ServicioPersonajes servicioPersonajes) {
        this.servicioPersonajes = servicioPersonajes;
    }

    @PostMapping("/save")
    public ResponseEntity<PersonajesEntity> guardarPersonaje(@RequestBody PersonajesEntity personaje) {
        PersonajesEntity nuevoPersonaje = servicioPersonajes.save(personaje);
        return new ResponseEntity<>(nuevoPersonaje, HttpStatus.CREATED);
    }

    @PutMapping("/modify")
    public ResponseEntity<PersonajesEntity> modificarPersonaje(
            @RequestParam String id,
            @RequestParam String campo,
            @RequestParam String valor
    ) {
        PersonajesEntity personajeModificado = servicioPersonajes.modify(id, campo, valor);
        if (personajeModificado != null) {
            return new ResponseEntity<>(personajeModificado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<PersonajesEntity>> obtenerTodosLosPersonajes() {
        List<PersonajesEntity> listaPersonajes = servicioPersonajes.getFullList();
        return new ResponseEntity<>(listaPersonajes, HttpStatus.OK);
    }

    @GetMapping("/getById")
    public ResponseEntity<PersonajesEntity> obtenerPersonajePorId(@RequestParam String id) {
        PersonajesEntity personaje = servicioPersonajes.getById(id);
        if (personaje != null) {
            return new ResponseEntity<>(personaje, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("delete")
    public ResponseEntity<Void> borrarPersonaje(@RequestParam String id) {
        servicioPersonajes.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/getByImagen")
    public ResponseEntity<PersonajesEntity> obtenerPersonajePorImagen(@RequestBody PersonajesEntity per) {
        PersonajesEntity personaje = servicioPersonajes.getByImagen(per.getImagen());
        if (personaje != null) {
            return new ResponseEntity<>(personaje, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
