package com.syw.APIrest.Stadistics.Services;

import com.syw.APIrest.Stadistics.Entitys.CartasEntity;
import com.syw.APIrest.Stadistics.Entitys.PersonajesEntity;
import com.syw.APIrest.Stadistics.Repositories.CartasRepository;
import com.syw.APIrest.Stadistics.Repositories.PersonajesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioCartas {

    private CartasRepository repository;

    @Autowired
    public ServicioCartas(CartasRepository repository) {
        this.repository = repository;
    }

    public CartasEntity save(CartasEntity carta) {
        return repository.save(carta);
    }

    public CartasEntity modify(String id, String campo, String valor) {
        Optional<CartasEntity> optionalCartas = repository.findById(id);
        return optionalCartas.map(carta -> {
            switch (campo) {
                case "tipo":
                    carta.setTipo(valor);
                    break;
                case "naturaleza":
                    carta.setNaturaleza(valor);
                    break;
                case "imagen":
                    carta.setImagen(valor);
                    break;
                case "cantidad":
                    carta.setCantidad(Integer.parseInt(valor));
                    break;
                default:
                    throw new IllegalArgumentException("Campo no válido");
            }
            return repository.save(carta);
        }).orElse(null); // Devuelve null si no se encuentra el ID del personaje
    }

    public List<CartasEntity> getFullList() {
        return repository.findAll();
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public CartasEntity getById(String id) {
        Optional<CartasEntity> optionalCarta = repository.findById(id);
        return optionalCarta.orElse(null); // Devuelve null si no se encuentra el ID del personaje
    }

}
