package com.utad.android.code.modelo;

import com.google.gson.Gson;
import com.utad.android.entitys.PersonajesEntity;
import com.utad.android.storage.Mochila;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConexionPersonajes {

    private static final String ROOT = "http://10.0.2.2:5001/api/Personajes/";

    public static PersonajesEntity getPersonajePorImagen(String idImagen) {
        PersonajesEntity result = null;

        try {
            URL url = new URL(ROOT + "getByImagen");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", "Bearer " + Mochila.token);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            PersonajesEntity personaje = new PersonajesEntity(idImagen);
            personaje.setImagen(idImagen);

            Gson gson = new Gson();
            String jsonInputString = gson.toJson(personaje);

            try (OutputStream os = urlConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                result = gson.fromJson(response.toString(), PersonajesEntity.class);
            } else {
                System.out.println("Error en la respuesta del servidor: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
