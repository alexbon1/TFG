package com.syw.APIrest.Stadistics.Controllers;

import com.syw.APIrest.Stadistics.Entitys.ArmadurasEntity;
import com.syw.APIrest.Stadistics.Entitys.ArmasEntity;
import com.syw.APIrest.Stadistics.Services.ServicioArmaduras;
import com.syw.APIrest.Stadistics.Services.ServicioArmas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Equipo")
public class ControladorEquipo {
    @Autowired
    private ServicioArmaduras ServicioArmaduras;
    @Autowired
    private ServicioArmas ServicioArmas;
    @PostMapping("/getMultiplesArmas")
    public ResponseEntity<List<ArmasEntity>> getMultiplesArmas(@RequestBody List<String> request) {
        List<ArmasEntity> armas = new LinkedList<>();
        for (int i = 0; i < request.size(); i++) {
           Optional<ArmasEntity> aux = ServicioArmas.getByID(request.get(i));
            if (aux.isPresent()){
                armas.add(aux.get());
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(armas,HttpStatus.OK);

    }
    @PostMapping("/getMultiplesArmaduras")
    public ResponseEntity<List<ArmadurasEntity>> getMultiplesArmaduras(@RequestBody List<String> request) {
        List<ArmadurasEntity> armaduras = new LinkedList<>();
        for (int i = 0; i < request.size(); i++) {
            Optional<ArmadurasEntity> aux = ServicioArmaduras.getByID(request.get(i));
            if (aux.isPresent()){
                armaduras.add(aux.get());
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(armaduras,HttpStatus.OK);
    }
}
