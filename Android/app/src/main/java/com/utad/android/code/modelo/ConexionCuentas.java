package com.utad.android.code.modelo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.utad.android.entitys.JSON.UsuarioJSON;
import com.utad.android.entitys.UsersEntity;
import com.utad.android.storage.Mochila;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConexionCuentas {

    private static final String ROOT = "http://10.0.2.2:5001/api/Cuentas/";

    public static UsuarioJSON login(UsersEntity loginRequest) {
        UsuarioJSON result = null;

        try {
            URL url = new URL(ROOT + "login");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", "Bearer " + Mochila.token);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            Gson gson = new Gson();
            String jsonInputString = gson.toJson(loginRequest);

            try (OutputStream os = urlConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                result = gson.fromJson(response.toString(), UsuarioJSON.class);
            } else {
                System.out.println("Error en la respuesta del servidor: " + urlConnection.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean existeUser(String cuerpo) {
        boolean existe = false;

        try {
            URL apiUrl = new URL(ROOT + "existeUser" + "/" + cuerpo);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestProperty("Authorization", "Bearer " + Mochila.token);
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                existe = Boolean.parseBoolean(response.toString());
            } else {
                System.out.println("Error en la respuesta del servidor: " + connection.getResponseCode());
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return existe;
    }

    public static boolean signIn(UsersEntity user) {
        try {
            Gson gson = new Gson();
            String jsonUser = gson.toJson(user);
            JsonObject jsonObject = gson.fromJson(jsonUser, JsonObject.class);
            jsonUser = jsonObject.toString();

            URL apiUrl = new URL(ROOT + "registro");
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + Mochila.token);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(jsonUser);
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                System.out.println("Registro exitoso.");
                return true;
            } else {
                System.out.println("Error en el registro. Código de respuesta: " + responseCode);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static UsuarioJSON modifyUsuario(int id, String columna, String valor) {
        UsuarioJSON result = null;

        try {
            URL apiUrl = new URL(ROOT + "modify");
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + Mochila.token);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            Gson gson = new Gson();
            String jsonBody = "{\"id\":" + id + ", \"columna\":\"" + columna + "\", \"valor\":\"" + valor + "\"}";

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(jsonBody);
            writer.flush();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                result = gson.fromJson(response.toString(), UsuarioJSON.class);
            } else {
                System.out.println("Error en la respuesta del servidor: " + connection.getResponseCode());
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}

