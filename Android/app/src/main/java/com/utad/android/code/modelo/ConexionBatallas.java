package com.utad.android.code.modelo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.utad.android.entitys.UsersEntity;
import com.utad.android.storage.Mochila;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

public class ConexionBatallas {
    private static final String ROOT = "http://10.0.2.2:5001/Batallas/";

    public static Set<UsersEntity> UsersEspera(UsersEntity loginRequest) {
        Set<UsersEntity> result = null;

        try {
            URL url = new URL(ROOT + "listaEspera");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", "Bearer " + Mochila.token);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            // Convertir el objeto loginRequest a JSON
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(loginRequest);

            // Escribir el JSON en el cuerpo de la solicitud
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

                // Definir el tipo para la deserialización
                Type setType = new TypeToken<Set<UsersEntity>>() {}.getType();
                result = gson.fromJson(response.toString(), setType);
            } else {
                System.out.println("Error en la respuesta del servidor: " + urlConnection.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static Integer BatallaNueva(UsersEntity loginRequest) {
        int result = -100;

        try {
            URL url = new URL(ROOT + "BatallaNueva");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", "Bearer " + Mochila.token);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            // Convertir el objeto LoginRequest a JSON
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(loginRequest);

            // Escribir el JSON en el cuerpo de la solicitud
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
                result = gson.fromJson(response.toString(), Integer.class);
            } else {
                System.out.println("Error en la respuesta del servidor: " + urlConnection.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static Integer JoinBatalla(UsersEntity loginRequest) {
        int result = -100;

        try {
            URL url = new URL(ROOT + "joinBatalla");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", "Bearer " + Mochila.token);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            // Convertir el objeto LoginRequest a JSON
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(loginRequest);

            // Escribir el JSON en el cuerpo de la solicitud
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
                result = gson.fromJson(response.toString(), Integer.class);
            } else {
                System.out.println("Error en la respuesta del servidor: " + urlConnection.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
