package com.syw.APIrest.Batallas.Operations;

import java.io.IOException;
import java.net.ServerSocket;

public class ReceptorPrincipalBatallas implements Runnable {

    public VariablesGeneralesBatallas vGenerales;

    public ReceptorPrincipalBatallas(int puerto) {
        this.vGenerales = new VariablesGeneralesBatallas();
        vGenerales.puerto = puerto;
    }

    @Override
    public void run() {
        iniciar();
    }

    public void iniciar() {
        vGenerales.booleanosGenerales.enCurso = true;
        try {
            vGenerales.serverSocket = new ServerSocket(vGenerales.puerto);
            while (vGenerales.booleanosGenerales.enCurso) {
                try {
                    if (vGenerales.booleanosGenerales.connection) {
                        PeticionConexion.procesar(vGenerales);
                    } else if (vGenerales.booleanosGenerales.start) {
                        vGenerales = PeticionStart.procesar(vGenerales);
                        vGenerales.booleanosGenerales.start = false;
                        vGenerales.booleanosGenerales.apuestas = true;
                    } else if (vGenerales.booleanosGenerales.apuestas) {
                        vGenerales = PeticionApuestas.procesar(vGenerales);
                    }else if (vGenerales.booleanosGenerales.cara) {
                        vGenerales=PeticionDecision.procesar(vGenerales);
                    } else if (vGenerales.booleanosGenerales.partida) {
                        vGenerales=PeticionRondas.procesar(vGenerales);
                    }
                } catch (IOException e) {
                    vGenerales.booleanosGenerales.enCurso = false;
                    break;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer getPuerto() {
        return vGenerales.puerto;
    }

    public boolean getIsCerrada() {
        return vGenerales.booleanosGenerales.cerrada;
    }
}
