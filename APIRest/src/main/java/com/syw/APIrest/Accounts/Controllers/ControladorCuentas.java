package com.syw.APIrest.Accounts.Controllers;

import com.syw.APIrest.Accounts.Entitys.UsersEntity;
import com.syw.APIrest.Accounts.ResJSON.UsuarioJSON;
import com.syw.APIrest.Accounts.Services.ServicioUsuarios;
import com.syw.APIrest.Seguridad.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/Cuentas")
public class ControladorCuentas {

    @Autowired
    private ServicioUsuarios Servicio;
    @Autowired
    private EncryptionUtil EncryptionUtil;
    @PostMapping("/login")
    public ResponseEntity<UsuarioJSON> login(@RequestBody UsersEntity loginRequest) {
        String username = loginRequest.getUsername();
        String pwd = loginRequest.getPwd();

        UsersEntity user = Servicio.getByUsername(username);
        if (user != null) {
            if (pwd.equals(user.getPwd())) {
                return new ResponseEntity<>(new UsuarioJSON(user), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<Void> registro(@RequestBody UsersEntity user) {
        return new ResponseEntity<>(Servicio.save(user) ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/existeUser/{username}")
    public ResponseEntity<Boolean> existeUser(@PathVariable String username) {
        com.syw.APIrest.Accounts.Entitys.UsersEntity user = Servicio.getByUsername(username);
        boolean userExists = user != null;
        return ResponseEntity.ok(userExists);
    }

    @PostMapping("/modify")
    public ResponseEntity<UsuarioJSON> modifyUser(@RequestBody Map<String, Object> modifyRequest) {
        // Extraer los datos del cuerpo de la solicitud
        int id = (int) modifyRequest.get("id");
        String columna = (String) modifyRequest.get("columna");
        String valor = (String) modifyRequest.get("valor");

        // Llamar al servicio para modificar al usuario
        UsersEntity modifiedUser = Servicio.modify(id, columna, valor);

        // Manejar la respuesta según el resultado de la modificación
        if (modifiedUser != null) {
            modifiedUser.setPwd(EncryptionUtil.decrypt(modifiedUser.getPwd()));
            return ResponseEntity.ok(new UsuarioJSON(modifiedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
