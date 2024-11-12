package com.utad.android.code.modelo;

import com.google.gson.Gson;
import com.utad.android.entitys.JSON.MisionCompletaJSON;
import com.utad.android.entitys.JSON.MisionesDiariasJSON;
import com.utad.android.entitys.UsersEntity;
import com.utad.android.storage.Mochila;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ConexionMisiones {

    private static final String ROOT = "http://10.0.2.2:5001/api/Misiones/";

    public static MisionesDiariasJSON misionDiariaBase() {
        MisionesDiariasJSON result = null;

        try {
            URL url = new URL(ROOT + "MisionDiaria");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", "Bearer " + Mochila.token);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            Gson gson = new Gson();
            String jsonInputString = gson.toJson(Mochila.user);

            try (OutputStream os = urlConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
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

                result = gson.fromJson(response.toString(), MisionesDiariasJSON.class);
            } else {
                System.out.println("Error en la respuesta del servidor: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static MisionCompletaJSON completarMision(MisionCompletaJSON misionesDiariasJSON) {
        MisionCompletaJSON result = null;

        try {
            URL url = new URL(ROOT + "completarMision");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", "Bearer " + Mochila.token);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            Gson gson = new Gson();
            String jsonInputString = gson.toJson(misionesDiariasJSON);

            try (OutputStream os = urlConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
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

                result = gson.fromJson(response.toString(), MisionCompletaJSON.class);
            } else {
                System.out.println("Error en la respuesta del servidor: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static MisionesDiariasJSON misionExtra() {
        MisionesDiariasJSON result = null;

        try {
            URL url = new URL(ROOT + "MisionExtra");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", "Bearer " + Mochila.token);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            Gson gson = new Gson();
            String jsonInputString = gson.toJson(Mochila.user);

            try (OutputStream os = urlConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
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

                result = gson.fromJson(response.toString(), MisionesDiariasJSON.class);
            } else {
                System.out.println("Error en la respuesta del servidor: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
