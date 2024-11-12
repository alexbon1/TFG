package com.syw.APIrest.Batallas.Threads;

import com.syw.APIrest.Batallas.InfoBatalla;
import com.syw.APIrest.Batallas.Operations.VariablesGeneralesBatallas;

import java.io.IOException;

public class HiloComprobante extends Thread {

    public VariablesGeneralesBatallas variables;

    public HiloComprobante(VariablesGeneralesBatallas variables) {
        this.variables = variables;
    }

    @Override
    public void run() {
        inicio();
    }

    public void inicio() {
        while (true) {
            try {
                InfoBatalla comprobante = new InfoBatalla();
                comprobante.user = InfoBatalla.USERS.SERVER;
                comprobante.tipo = InfoBatalla.TIPOS.MENSAJE;
                comprobante.mensaje = InfoBatalla.MENSAJES.PING;

                if (variables.vComprobante.revisar1) {
                    variables.usuario1Writer.writeObject(comprobante.toString());
                    variables.usuario1Writer.flush();
                }
                if (variables.vComprobante.revisar2) {
                    variables.usuario2Writer.writeObject(comprobante.toString());
                    variables.usuario2Writer.flush();
                }

                Thread.sleep(1000);
            } catch (IOException | InterruptedException e) {
                handleDisconnection();
                cerrarRecursos();
                break;
            }
        }
    }

    private void handleDisconnection() {
        try {
            if (variables.vComprobante.revisar1) {
                InfoBatalla desconexion = new InfoBatalla();
                desconexion.user = InfoBatalla.USERS.SERVER;
                desconexion.tipo = InfoBatalla.TIPOS.CIERRE;
                desconexion.mensaje = InfoBatalla.MENSAJES.CIERRES.DESCUSER1;
                if (variables.vComprobante.revisar2) {
                    variables.usuario2Writer.writeObject(desconexion.toString());
                    variables.usuario2Writer.flush();
                }
            }
        } catch (IOException ignored) {
        }

        try {
            if (variables.vComprobante.revisar1) {
                InfoBatalla desconexion = new InfoBatalla();
                desconexion.user = InfoBatalla.USERS.SERVER;
                desconexion.tipo = InfoBatalla.TIPOS.CIERRE;
                desconexion.mensaje = InfoBatalla.MENSAJES.CIERRES.DESCUSER2;
                variables.usuario1Writer.writeObject(desconexion.toString());
                variables.usuario1Writer.flush();
            }
        } catch (IOException ignored) {
        }
    }

    private void cerrarRecursos() {
        variables.booleanosGenerales.enCurso = false;
        try {
            if (variables.usuario1Reader != null) {
                try {
                    variables.usuario1Reader.close();
                } catch (IOException ignored) {
                }
            }
            if (variables.usuario1Writer != null) {
                try {
                    variables.usuario1Writer.close();
                } catch (IOException ignored) {
                }
            }
            if (variables.usuario2Reader != null) {
                try {
                    variables.usuario2Reader.close();
                } catch (IOException ignored) {
                }
            }
            if (variables.usuario2Writer != null) {
                try {
                    variables.usuario2Writer.close();
                } catch (IOException ignored) {
                }
            }
            if (variables.usuario1Socket != null && !variables.usuario1Socket.isClosed()) {
                try {
                    variables.usuario1Socket.close();
                } catch (IOException ignored) {
                }
            }
            if (variables.usuario2Socket != null && !variables.usuario2Socket.isClosed()) {
                try {
                    variables.usuario2Socket.close();
                } catch (IOException ignored) {
                }
            }
            if (variables.serverSocket != null && !variables.serverSocket.isClosed()) {
                try {
                    variables.serverSocket.close();
                } catch (IOException ignored) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Liberado el puerto: " + variables.puerto);
    }
}
