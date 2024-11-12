package com.syw.APIrest.Batallas.Controllers;

import com.syw.APIrest.Accounts.Entitys.UsersEntity;
import com.syw.APIrest.Batallas.Services.ServicioBatallas;
import com.syw.APIrest.Batallas.Threads.HiloBatalla;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/Batallas")
public class ControladorBatallas {

    private final Map<UsersEntity, HiloBatalla> waitingUsers = new ConcurrentHashMap<>();
    private final ServicioBatallas servicioBatallas;

    @Autowired
    public ControladorBatallas(ServicioBatallas servicioBatallas) {
        this.servicioBatallas = servicioBatallas;
    }

    @PostMapping("/BatallaNueva")
    public ResponseEntity<Integer> batallaNueva(@RequestBody UsersEntity user) {
        if (waitingUsers.containsKey(user)) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } else {
            int puerto = ServicioBatallas.primerPuertoDisponible(); // Puerto de ejemplo
            waitingUsers.put(user, servicioBatallas.iniciarBatalla(puerto));
            return new ResponseEntity<>(puerto, HttpStatus.OK);
        }
    }

    @PostMapping("/joinBatalla")
    public ResponseEntity<Integer> joinBatalla(@RequestBody UsersEntity enemigo) {
        comprobarLista();
        HiloBatalla batallaThread = waitingUsers.get(enemigo);
        if (batallaThread!=null){
            return new ResponseEntity<>(batallaThread.getBatalla().getPuerto(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/listaEspera")
    public ResponseEntity<Set<UsersEntity>> listaEspera() {
        comprobarLista();
        Set<UsersEntity> ret = waitingUsers.keySet();
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    private void comprobarLista() {
        Set<UsersEntity> usersWaiting = waitingUsers.keySet();
        for (UsersEntity user : usersWaiting) {
            HiloBatalla hilo = waitingUsers.get(user);
            if (!hilo.isAlive() || hilo.getBatalla().getIsCerrada()) {
                waitingUsers.remove(user);
            }
        }
    }


}
