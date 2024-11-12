package com.syw.APIrest.Stadistics.Controllers;

import com.syw.APIrest.Accounts.Entitys.UsersEntity;
import com.syw.APIrest.Accounts.Services.ServicioUsuarios;
import com.syw.APIrest.Stadistics.Constantes.ConstantesMonedas;
import com.syw.APIrest.Stadistics.Constantes.ConstantesXP;
import com.syw.APIrest.Stadistics.Entitys.HistoricoEntity;
import com.syw.APIrest.Stadistics.Entitys.MisionesDiariasEntity;
import com.syw.APIrest.Stadistics.ResJSON.MisionCompletaJSON;
import com.syw.APIrest.Stadistics.ResJSON.MisionesDiariasJSON;
import com.syw.APIrest.Stadistics.Services.ServicioHistorico;
import com.syw.APIrest.Stadistics.Services.ServicioMisionesDiarias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;


@RestController
@RequestMapping("/api/Misiones")
public class ControladorMisiones {
    private final ServicioMisionesDiarias SevicioMisionesDiarias;
    private final ServicioHistorico ServicioHistorico;
    private final ServicioUsuarios ServicioUsuarios;

    @Autowired
    public ControladorMisiones(ServicioMisionesDiarias sevicioMisionesDiarias, ServicioHistorico servicioHistoricoMisionesDiarias, com.syw.APIrest.Accounts.Services.ServicioUsuarios servicioUsuarios) {
        SevicioMisionesDiarias = sevicioMisionesDiarias;
        ServicioHistorico = servicioHistoricoMisionesDiarias;
        ServicioUsuarios = servicioUsuarios;
    }


    @PostMapping("/MisionDiaria")
    public ResponseEntity<MisionesDiariasJSON> obtenerMisionDiaria(@RequestBody UsersEntity user) {
        List<MisionesDiariasEntity> listaMisiones = SevicioMisionesDiarias.getByTipoUser(user.getTipo());
        LocalDate fechaActual = LocalDate.now();
        String seccion = "Misiones/" + user.getTipo();
        Optional<HistoricoEntity> misionHoy = ServicioHistorico.getByFechaAndSeccion(fechaActual.toString(), seccion);
        if (misionHoy.isPresent()) {
            HistoricoEntity historicoEntity = misionHoy.get();
            String hid = historicoEntity.getIdObject();
            MisionesDiariasEntity misionesDiariasEntity = SevicioMisionesDiarias.getByID(hid).get();
            MisionesDiariasJSON ret = new MisionesDiariasJSON(misionesDiariasEntity, ConstantesMonedas.Misiones.RECOMPENSA_DIARIAS, ConstantesXP.Misiones.DIARIAS);
            return new ResponseEntity<>(ret, HttpStatus.OK);
        } else {
            limpiarUsers();
            Random random = new Random();
            MisionesDiariasJSON ret = null;
            if (listaMisiones.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            } else if (listaMisiones.size() == 1) {
                ret = new MisionesDiariasJSON(listaMisiones.get(0), ConstantesMonedas.Misiones.RECOMPENSA_DIARIAS, ConstantesXP.Misiones.DIARIAS);
                if (ret == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                int numeroAleatorio = random.nextInt(listaMisiones.size() - 1);
                ret = new MisionesDiariasJSON(listaMisiones.get(numeroAleatorio), ConstantesMonedas.Misiones.RECOMPENSA_DIARIAS, ConstantesXP.Misiones.DIARIAS);
                if (ret == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
            ServicioHistorico.save(new HistoricoEntity(fechaActual.toString(), "Misiones/" + user.getTipo(), ret.getId()));
            return new ResponseEntity<>(ret, HttpStatus.OK);
        }
    }

    private void limpiarUsers() {
        for (UsersEntity usersEntity : ServicioUsuarios.getFullList()) {
            ServicioUsuarios.modify(usersEntity.getId(), "misiones", String.format("%d", 0));
        }
    }

    @PostMapping("/MisionExtra")
    public ResponseEntity<MisionesDiariasJSON> obtenerMisionExtra(@RequestBody UsersEntity user) {
        List<MisionesDiariasEntity> listaMisiones = SevicioMisionesDiarias.getByTipoUser(user.getTipo());
        LocalDate fechaActual = LocalDate.now();
        String seccion = "Misiones/" + user.getTipo();
        Optional<HistoricoEntity> misionHoy = ServicioHistorico.getByFechaAndSeccion(fechaActual.toString(), seccion);
        ServicioUsuarios.modify(user.getId(), "monedas", String.valueOf(user.getMonedas() - ConstantesMonedas.Misiones.MISION_EXTRA));
        if (misionHoy.isPresent()) {
            for (int i = 0; i < listaMisiones.size(); i++) {
                if (listaMisiones.get(i).getId().equals(misionHoy.get().getIdObject())) {
                    listaMisiones.remove(i);
                    break;
                }
            }
            Random random = new Random();
            if (listaMisiones.size() <= 0) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            } else if (listaMisiones.size() == 1) {
                MisionesDiariasJSON ret = new MisionesDiariasJSON(listaMisiones.get(0), ConstantesMonedas.Misiones.RECOMPENSA_DIARIAS, ConstantesXP.Misiones.DIARIAS);
                return new ResponseEntity<>(ret, HttpStatus.OK);
            } else {
                int numeroAleatorio = random.nextInt(listaMisiones.size() - 1);
                MisionesDiariasJSON ret = new MisionesDiariasJSON(listaMisiones.get(numeroAleatorio), ConstantesMonedas.Misiones.RECOMPENSA_DIARIAS, ConstantesXP.Misiones.DIARIAS);
                return new ResponseEntity<>(ret, HttpStatus.OK);
            }

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/completarMision")
    public ResponseEntity<MisionCompletaJSON> completarMision(@RequestBody MisionCompletaJSON completa) {
        Optional<MisionesDiariasEntity> present = SevicioMisionesDiarias.getByID(completa.idMision);
        if (present.isPresent()) {
            double multi = 1 + (completa.porcentaje - 1) / 99.0;
            int recompensa1 = (int) (ConstantesMonedas.Misiones.RECOMPENSA_DIARIAS * multi);
            int recompensa2 = (int) (ConstantesXP.Misiones.DIARIAS * multi);
            completa.resultado += "\nMonedas: " + recompensa1 + "\nXP: " + recompensa2;
            ServicioUsuarios.modify(completa.user.getId(), "monedas", String.format("%d", completa.user.getMonedas() + recompensa1));
            ServicioUsuarios.modify(completa.user.getId(), "nivel", String.format("%d", completa.user.getNivel() + recompensa2));
            ServicioUsuarios.modify(completa.user.getId(), "misiones", String.format("%d", 1));
            completa.user = ServicioUsuarios.getByID(completa.user.getId());
            return new ResponseEntity<>(completa, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
