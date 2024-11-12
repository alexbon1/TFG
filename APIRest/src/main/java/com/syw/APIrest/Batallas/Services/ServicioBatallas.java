package com.syw.APIrest.Batallas.Services;

import com.syw.APIrest.Accounts.Services.ServicioInventarios;
import com.syw.APIrest.Accounts.Services.ServicioUsuarios;
import com.syw.APIrest.Batallas.Operations.ReceptorPrincipalBatallas;
import com.syw.APIrest.Batallas.Threads.HiloBatalla;
import com.syw.APIrest.Stadistics.Services.ServicioArmaduras;
import com.syw.APIrest.Stadistics.Services.ServicioArmas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;

@Service
public class ServicioBatallas {

    private static ServicioUsuarios servicioUsuariosStatic;
    private static ServicioArmas servicioArmasStatic;
    private static ServicioArmaduras servicioArmadurasStatic;
    private static ServicioInventarios servicioInventariosStatic;

    private final ExecutorService battleExecutor;
    private final ServicioUsuarios servicioUsuarios;
    private final ServicioArmas servicioArmas;
    private final ServicioArmaduras servicioArmaduras;
    private final ServicioInventarios servicioInventarios;

    @Autowired
    public ServicioBatallas(ExecutorService battleExecutor, ServicioUsuarios servicioUsuarios, ServicioArmas servicioArmas, ServicioArmaduras servicioArmaduras, ServicioInventarios servicioInventarios) {
        this.battleExecutor = battleExecutor;
        this.servicioUsuarios = servicioUsuarios;
        this.servicioArmas = servicioArmas;
        this.servicioArmaduras = servicioArmaduras;
        this.servicioInventarios = servicioInventarios;

        // Asignación a variables estáticas
        ServicioBatallas.servicioUsuariosStatic = servicioUsuarios;
        ServicioBatallas.servicioArmasStatic = servicioArmas;
        ServicioBatallas.servicioArmadurasStatic = servicioArmaduras;
        ServicioBatallas.servicioInventariosStatic = servicioInventarios;
    }

    public HiloBatalla iniciarBatalla(int puerto) {
        ReceptorPrincipalBatallas receptorPrincipalBatallas = new ReceptorPrincipalBatallas(puerto);
        HiloBatalla thread = new HiloBatalla(receptorPrincipalBatallas);
        thread.start();
        return thread;
    }

    public static int primerPuertoDisponible() {
        int puertoInicial = 5555;

        for (int puerto = puertoInicial; puerto <= 65535; puerto++) { // 65535 es el máximo número de puerto
            if (puertoDisponible(puerto)) {
                return puerto;
            }
        }

        return -1; // Devuelve -1 si no se encuentra ningún puerto disponible
    }

    private static boolean puertoDisponible(int puerto) {
        try (ServerSocket ignored = new ServerSocket(puerto)) {
            return true; // Si no se lanza una excepción al crear el ServerSocket, el puerto está disponible
        } catch (IOException e) {
            return false; // Si se lanza una excepción al crear el ServerSocket, el puerto no está disponible
        }
    }

    public static ServicioUsuarios getServicioUsuarios() {
        return servicioUsuariosStatic;
    }

    public static ServicioArmas getServicioArmas() {
        return servicioArmasStatic;
    }

    public static ServicioArmaduras getServicioArmaduras() {
        return servicioArmadurasStatic;
    }

    public static ServicioInventarios getServicioInventarios() {
        return servicioInventariosStatic;
    }
}
