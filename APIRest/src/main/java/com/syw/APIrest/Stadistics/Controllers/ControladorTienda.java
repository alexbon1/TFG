package com.syw.APIrest.Stadistics.Controllers;

import com.syw.APIrest.Accounts.Entitys.InventariosEntity;
import com.syw.APIrest.Accounts.Entitys.UsersEntity;
import com.syw.APIrest.Accounts.Services.ServicioInventarios;
import com.syw.APIrest.Accounts.Services.ServicioUsuarios;
import com.syw.APIrest.Stadistics.Constantes.ConstantesMonedas;
import com.syw.APIrest.Stadistics.Constantes.ConstantesTienda;
import com.syw.APIrest.Stadistics.Entitys.ArmadurasEntity;
import com.syw.APIrest.Stadistics.Entitys.ArmasEntity;
import com.syw.APIrest.Stadistics.Entitys.HistoricoEntity;
import com.syw.APIrest.Stadistics.ResJSON.BoxJSON;
import com.syw.APIrest.Stadistics.ResJSON.CompraJSON;
import com.syw.APIrest.Stadistics.ResJSON.TiendaDiaJSON;
import com.syw.APIrest.Stadistics.Services.ServicioArmaduras;
import com.syw.APIrest.Stadistics.Services.ServicioArmas;
import com.syw.APIrest.Stadistics.Services.ServicioHistorico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/Tienda")
public class ControladorTienda {
    @Autowired
    ServicioArmaduras servicioArmaduras;
    @Autowired
    ServicioArmas servicioArmas;
    @Autowired
    ServicioInventarios servicioInventarios;
    @Autowired
    ServicioHistorico servicioHistorico;
    @Autowired
    ServicioUsuarios servicioUsuarios;

    @PostMapping("/TiendaDiaria")
    public ResponseEntity<TiendaDiaJSON> obtenerTiendaDiaria(@RequestBody UsersEntity user) throws IOException, ClassNotFoundException {
        LocalDate fechaActual = LocalDate.now();

        Optional<HistoricoEntity> tiendaHistorico = servicioHistorico.getByFechaAndSeccion(fechaActual.toString(), "Tienda");
        if (tiendaHistorico.isEmpty()) {
            Random random = new Random();
            List<ArmasEntity> armasSeleccionadas = new LinkedList<>();
            List<ArmadurasEntity> armadurasSeleccionadas = new LinkedList<>();

            for (int i = 0; i <= 2; i++) {
                int probabilidadGeneral = random.nextInt(100);
                if (probabilidadGeneral <= ConstantesTienda.Probabilidades.ARMADURAS) {
                    int probabilidadRaroComun = random.nextInt(100);
                    if (probabilidadRaroComun <= ConstantesTienda.Probabilidades.COMUN_RARO[0]) {
                        List<ArmadurasEntity> armadurasDisponibles = servicioArmaduras.getByTipo("Comun");
                        armadurasDisponibles.removeAll(armadurasSeleccionadas);
                        if (!armadurasDisponibles.isEmpty()) {
                            int indiceSeleccionado = random.nextInt(armadurasDisponibles.size());
                            armadurasSeleccionadas.add(armadurasDisponibles.get(indiceSeleccionado));
                        }
                    } else if (probabilidadRaroComun >= ConstantesTienda.Probabilidades.COMUN_RARO[1]) {
                        List<ArmadurasEntity> armadurasDisponibles = servicioArmaduras.getByTipo("Raro");
                        armadurasDisponibles.removeAll(armadurasSeleccionadas);
                        if (!armadurasDisponibles.isEmpty()) {
                            int indiceSeleccionado = random.nextInt(armadurasDisponibles.size());
                            armadurasSeleccionadas.add(armadurasDisponibles.get(indiceSeleccionado));
                        }
                    }
                } else if (probabilidadGeneral <= 100) {
                    int probabilidadRaroComun = random.nextInt(99) + 1;
                    if (probabilidadRaroComun <= ConstantesTienda.Probabilidades.COMUN_RARO[0]) {
                        List<ArmasEntity> armasDisponibles = servicioArmas.getByTipo("Comun");
                        armasDisponibles.removeAll(armasSeleccionadas);
                        if (!armasDisponibles.isEmpty()) {
                            int indiceSeleccionado = random.nextInt(armasDisponibles.size());
                            armasSeleccionadas.add(armasDisponibles.get(indiceSeleccionado));
                        }
                    } else if (probabilidadRaroComun >= ConstantesTienda.Probabilidades.COMUN_RARO[1]) {
                        List<ArmasEntity> armasDisponibles = servicioArmas.getByTipo("Raro");
                        armasDisponibles.removeAll(armasSeleccionadas);
                        if (!armasDisponibles.isEmpty()) {
                            int indiceSeleccionado = random.nextInt(armasDisponibles.size());
                            armasSeleccionadas.add(armasDisponibles.get(indiceSeleccionado));
                        }
                    }
                }
            }

            int probabilidadEspecial = random.nextInt(100);
            if (probabilidadEspecial <= ConstantesTienda.Probabilidades.ESPECIAL_EPICO_LEGENDARIO[0]) {
                List<ArmasEntity> armasDisponibles = servicioArmas.getByTipo("Especial");
                armasDisponibles.removeAll(armasSeleccionadas);
                if (!armasDisponibles.isEmpty()) {
                    int indiceSeleccionado = random.nextInt(armasDisponibles.size());
                    armasSeleccionadas.add(armasDisponibles.get(indiceSeleccionado));
                }
            } else if (probabilidadEspecial <= ConstantesTienda.Probabilidades.ESPECIAL_EPICO_LEGENDARIO[1]) {
                List<ArmasEntity> armasDisponibles = servicioArmas.getByTipo("Epico");
                armasDisponibles.removeAll(armasSeleccionadas);
                if (!armasDisponibles.isEmpty()) {
                    int indiceSeleccionado = random.nextInt(armasDisponibles.size());
                    armasSeleccionadas.add(armasDisponibles.get(indiceSeleccionado));
                }
            } else if (probabilidadEspecial <= ConstantesTienda.Probabilidades.ESPECIAL_EPICO_LEGENDARIO[2]) {
                List<ArmasEntity> armasDisponibles = servicioArmas.getByTipo("Legendario");
                armasDisponibles.removeAll(armasSeleccionadas);
                if (!armasDisponibles.isEmpty()) {
                    int indiceSeleccionado = random.nextInt(armasDisponibles.size());
                    armasSeleccionadas.add(armasDisponibles.get(indiceSeleccionado));
                }
            }

            ArmadurasEntity[] arrayArmaduras = armadurasSeleccionadas.toArray(new ArmadurasEntity[0]);
            ArmasEntity[] arrayArmas = armasSeleccionadas.toArray(new ArmasEntity[0]);

            String strArmas = servicioInventarios.getByID(user.getId()).getArmas();
            String strArmaduras = servicioInventarios.getByID(user.getId()).getArmaduras();
            String[] armasInventario = strArmas != null ? strArmas.split("\\|") : new String[0];
            String[] armadurasInventario = strArmaduras != null ? strArmaduras.split("\\|") : new String[0];

            List<String> armasContiene = new LinkedList<>();
            List<String> armadurasContiene = new LinkedList<>();
            for (ArmasEntity arma : arrayArmas) {
                for (String id : armasInventario) {
                    if (arma.getId().equals(id)) {
                        armasContiene.add(id);
                    }
                }
            }

            for (ArmadurasEntity armadura : arrayArmaduras) {
                for (String id : armadurasInventario) {
                    if (armadura.getId().equals(id)) {
                        armadurasContiene.add(id);
                    }
                }
            }

            String[] arrayArmasCont = armasContiene.toArray(new String[0]);
            String[] arrayArmadurasCont = armadurasContiene.toArray(new String[0]);

            TiendaDiaJSON tienda = new TiendaDiaJSON(arrayArmas, arrayArmaduras, arrayArmasCont, arrayArmadurasCont);
            servicioHistorico.save(new HistoricoEntity(fechaActual.toString(), "Tienda", tienda.Serializar()));
            return new ResponseEntity<>(tienda, HttpStatus.OK);
        } else {
            TiendaDiaJSON recu = TiendaDiaJSON.Deserializar(tiendaHistorico.get().getIdObject());
            ArmadurasEntity[] arrayArmaduras = recu.getArmaduras();
            ArmasEntity[] arrayArmas = recu.getArmas();

            String strArmas = servicioInventarios.getByID(user.getId()).getArmas();
            String strArmaduras = servicioInventarios.getByID(user.getId()).getArmaduras();
            String[] armasInventario = strArmas != null ? strArmas.split("\\|") : new String[0];
            String[] armadurasInventario = strArmaduras != null ? strArmaduras.split("\\|") : new String[0];

            List<String> armasContiene = new LinkedList<>();
            List<String> armadurasContiene = new LinkedList<>();
            for (ArmasEntity arma : arrayArmas) {
                for (String id : armasInventario) {
                    if (arma.getId().equals(id)) {
                        armasContiene.add(id);
                    }
                }
            }

            for (ArmadurasEntity armadura : arrayArmaduras) {
                for (String id : armadurasInventario) {
                    if (armadura.getId().equals(id)) {
                        armadurasContiene.add(id);
                    }
                }
            }

            String[] arrayArmasCont = armasContiene.toArray(new String[0]);
            String[] arrayArmadurasCont = armadurasContiene.toArray(new String[0]);
            recu.setYaLasTienesArmaduras(arrayArmadurasCont);
            recu.setYaLasTienesArmas(arrayArmasCont);
            return new ResponseEntity<>(recu, HttpStatus.OK);
        }
    }

    @PostMapping("/compra")
    public ResponseEntity<InventariosEntity> comprarArma(@RequestBody CompraJSON compra) throws IOException, ClassNotFoundException {
        System.out.println("JSON recibido: " + compra);
        int monedas = servicioUsuarios.getByID(compra.user.getId()).getMonedas();
        int precio = 0;
        InventariosEntity ivAnt = servicioInventarios.getByID(compra.user.getId());
        if (compra.armadura != null) {
            precio = switch (compra.armadura.getRareza()) {
                case ConstantesTienda.Tipos.COMUNES -> ConstantesMonedas.Armaduras.PRECIO_COMUNES;
                case ConstantesTienda.Tipos.RARAS -> ConstantesMonedas.Armaduras.PRECIO_RARAS;
                case ConstantesTienda.Tipos.ESPECIALES -> ConstantesMonedas.Armaduras.PRECIO_ESPECIALES;
                case ConstantesTienda.Tipos.EPICAS -> ConstantesMonedas.Armaduras.PRECIO_EPICAS;
                case ConstantesTienda.Tipos.LEGENDARIAS -> ConstantesMonedas.Armaduras.PRECIO_LEGENDARIAS;
                default -> precio;
            };
            anyadirArmadura(ivAnt,compra.user, compra.armadura);

        } else if (compra.arma != null) {
            precio = switch ((compra.arma).getRareza()) {
                case ConstantesTienda.Tipos.COMUNES -> ConstantesMonedas.Armas.PRECIO_COMUNES;
                case ConstantesTienda.Tipos.RARAS -> ConstantesMonedas.Armas.PRECIO_RARAS;
                case ConstantesTienda.Tipos.ESPECIALES -> ConstantesMonedas.Armas.PRECIO_ESPECIALES;
                case ConstantesTienda.Tipos.EPICAS -> ConstantesMonedas.Armas.PRECIO_EPICAS;
                case ConstantesTienda.Tipos.LEGENDARIAS -> ConstantesMonedas.Armas.PRECIO_LEGENDARIAS;
                default -> precio;
            };
            anyadirArma(ivAnt,compra.user,compra.arma);
        }
        servicioUsuarios.modify(compra.user.getId(), "monedas", String.format("%d", monedas - precio));
        return new ResponseEntity<>(servicioInventarios.getByID(compra.user.getId()),HttpStatus.OK);
    }

    @PostMapping("/compra/boxBarata")
    public ResponseEntity<BoxJSON> comprarBoxBarata(@RequestBody UsersEntity user) {
        Random random = new Random();
        ArmasEntity arma = null;
        ArmadurasEntity armadura = null;
        int que = random.nextInt(100);
        boolean loTienes=false;
        int probabilidad = random.nextInt(100);
        if (que < ConstantesTienda.Probabilidades.ARMAS) {
            if (ConstantesTienda.Box.BARATA[0] <= probabilidad && probabilidad <= ConstantesTienda.Box.BARATA[1]) {
                List<ArmasEntity> armasDisponibles = servicioArmas.getByTipo("Comun");
                if (!armasDisponibles.isEmpty()) {
                    int indiceSeleccionado = random.nextInt(armasDisponibles.size());
                    arma = armasDisponibles.get(indiceSeleccionado);
                }
            } else if (ConstantesTienda.Box.BARATA[1] < probabilidad && probabilidad <= ConstantesTienda.Box.BARATA[2]) {
                List<ArmasEntity> armasDisponibles = servicioArmas.getByTipo("Raro");
                if (!armasDisponibles.isEmpty()) {
                    int indiceSeleccionado = random.nextInt(armasDisponibles.size());
                    arma = armasDisponibles.get(indiceSeleccionado);
                }
            } else {
                List<ArmasEntity> armasDisponibles = servicioArmas.getByTipo("Especial");
                if (!armasDisponibles.isEmpty()) {
                    int indiceSeleccionado = random.nextInt(armasDisponibles.size());
                    arma = armasDisponibles.get(indiceSeleccionado);
                }
            }
            loTienes=servicioInventarios.comprobarExistencia(user.getId(),arma);
            if (!loTienes){
                anyadirArma(servicioInventarios.getByID(user.getId()),user, arma);
            }
        } else {
            if (ConstantesTienda.Box.BARATA[0] <= probabilidad && probabilidad <= ConstantesTienda.Box.BARATA[1]) {
                List<ArmadurasEntity> armadurasDisponibles = servicioArmaduras.getByTipo("Comun");
                if (!armadurasDisponibles.isEmpty()) {
                    int indiceSeleccionado = random.nextInt(armadurasDisponibles.size());
                    armadura = armadurasDisponibles.get(indiceSeleccionado);
                }
            } else if (ConstantesTienda.Box.BARATA[1] < probabilidad && probabilidad <= ConstantesTienda.Box.BARATA[2]) {
                List<ArmadurasEntity> armadurasDisponibles = servicioArmaduras.getByTipo("Raro");
                if (!armadurasDisponibles.isEmpty()) {
                    int indiceSeleccionado = random.nextInt(armadurasDisponibles.size());
                    armadura = armadurasDisponibles.get(indiceSeleccionado);
                }
            } else {
                List<ArmadurasEntity> armadurasDisponibles = servicioArmaduras.getByTipo("Especial");
                if (!armadurasDisponibles.isEmpty()) {
                    int indiceSeleccionado = random.nextInt(armadurasDisponibles.size());
                    armadura = armadurasDisponibles.get(indiceSeleccionado);
                }
            }
            loTienes=servicioInventarios.comprobarExistencia(user.getId(),armadura);
            if (!loTienes){
                anyadirArmadura(servicioInventarios.getByID(user.getId()),user, armadura);
            }
        }
        int precio = ConstantesMonedas.Box.BARATAS;
        servicioUsuarios.modify(user.getId(), "monedas", String.format("%d", servicioUsuarios.getByID(user.getId()).getMonedas() - precio));
        return new ResponseEntity<>(new BoxJSON(armadura, arma,loTienes), HttpStatus.OK);
    }
    public void anyadirArma(InventariosEntity ivAnt,UsersEntity user,ArmasEntity arma){
        if (ivAnt.getArmas() == null || ivAnt.getArmas().isEmpty()) {
            servicioInventarios.modify(user.getId(), "armas", arma.getId());
        } else {
            servicioInventarios.modify(user.getId(), "armas", ivAnt.getArmas() + "|" + arma.getId());
        }
    }
    public void anyadirArmadura(InventariosEntity ivAnt,UsersEntity user,ArmadurasEntity armadura){
        if (ivAnt.getArmaduras() == null || ivAnt.getArmaduras().isEmpty()) {
            servicioInventarios.modify(user.getId(), "armaduras", armadura.getId());
        } else {
            servicioInventarios.modify(user.getId(), "armaduras", ivAnt.getArmaduras() + "|" + armadura.getId());
        }
    }
    @PostMapping("/compra/boxCara")
    public ResponseEntity<BoxJSON> comprarBoxCara(@RequestBody UsersEntity user) {
        Random random = new Random();
        ArmasEntity arma = null;
        ArmadurasEntity armadura = null;
        int que = random.nextInt(100);
        boolean loTienes = false;
        int probabilidad = random.nextInt(100);

        // Verifica si se obtiene un arma o una armadura
        if (que < ConstantesTienda.Probabilidades.ARMAS) {
            // Selección de arma según la probabilidad
            if (ConstantesTienda.Box.CARA[0] <= probabilidad && probabilidad <= ConstantesTienda.Box.CARA[1]) {
                List<ArmasEntity> armasDisponibles = servicioArmas.getByTipo("Epica");
                if (!armasDisponibles.isEmpty()) {
                    int indiceSeleccionado = random.nextInt(armasDisponibles.size());
                    arma = armasDisponibles.get(indiceSeleccionado);
                }
            } else {
                List<ArmasEntity> armasDisponibles = servicioArmas.getByTipo("Legendaria");
                if (!armasDisponibles.isEmpty()) {
                    int indiceSeleccionado = random.nextInt(armasDisponibles.size());
                    arma = armasDisponibles.get(indiceSeleccionado);
                }
            }

            // Comprobar existencia y añadir arma si no la tiene
            if (arma != null) {
                loTienes = servicioInventarios.comprobarExistencia(user.getId(), arma);
                if (!loTienes) {
                    anyadirArma(servicioInventarios.getByID(user.getId()), user, arma);
                }
            }
        } else {
            // Selección de armadura según la probabilidad
            if (ConstantesTienda.Box.CARA[0] <= probabilidad && probabilidad <= ConstantesTienda.Box.CARA[1]) {
                List<ArmadurasEntity> armadurasDisponibles = servicioArmaduras.getByTipo("Epica");
                if (!armadurasDisponibles.isEmpty()) {
                    int indiceSeleccionado = random.nextInt(armadurasDisponibles.size());
                    armadura = armadurasDisponibles.get(indiceSeleccionado);
                }
            } else {
                List<ArmadurasEntity> armadurasDisponibles = servicioArmaduras.getByTipo("Legendaria");
                if (!armadurasDisponibles.isEmpty()) {
                    int indiceSeleccionado = random.nextInt(armadurasDisponibles.size());
                    armadura = armadurasDisponibles.get(indiceSeleccionado);
                }
            }

            // Comprobar existencia y añadir armadura si no la tiene
            if (armadura != null) {
                loTienes = servicioInventarios.comprobarExistencia(user.getId(), armadura);
                if (!loTienes) {
                    anyadirArmadura(servicioInventarios.getByID(user.getId()), user, armadura);
                }
            }
        }

        // Modificación del saldo del usuario
        int precio = ConstantesMonedas.Box.CARAS;
        servicioUsuarios.modify(user.getId(), "monedas", String.format("%d", servicioUsuarios.getByID(user.getId()).getMonedas() - precio));

        return new ResponseEntity<>(new BoxJSON(armadura, arma, loTienes), HttpStatus.OK);
    }

}


