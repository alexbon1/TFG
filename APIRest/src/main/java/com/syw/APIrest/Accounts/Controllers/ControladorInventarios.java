package com.syw.APIrest.Accounts.Controllers;

import com.syw.APIrest.Accounts.Entitys.InventariosEntity;
import com.syw.APIrest.Accounts.Services.ServicioInventarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
@RequestMapping("/api/Inventarios")
public class ControladorInventarios {
    @Autowired
    private ServicioInventarios Servicio;
    @PostMapping("/modify")
    public ResponseEntity<InventariosEntity> modifyUser(@RequestBody Map<String, Object> modifyRequest) {
        // Extraer los datos del cuerpo de la solicitud
        int id = (int) modifyRequest.get("id");
        String columna = (String) modifyRequest.get("columna");
        String valor = (String) modifyRequest.get("valor");

        // Llamar al servicio para modificar al usuario
        InventariosEntity modifiedInventario = Servicio.modify(id, columna, valor);

        // Manejar la respuesta según el resultado de la modificación
        if (modifiedInventario != null) {
            return ResponseEntity.ok(modifiedInventario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/getByID")
    public ResponseEntity<InventariosEntity> getByID(@RequestBody InventariosEntity inventario) {
        InventariosEntity inventarioEncontrado = Servicio.getByID(inventario.getId());
        if (inventarioEncontrado != null) {
            return new ResponseEntity<>(inventarioEncontrado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
