package com.utad.android.code.modelo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.utad.android.entitys.ArmasEntity;
import com.utad.android.entitys.ArmadurasEntity;
import com.utad.android.storage.Mochila;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ConexionEquipo {

    private static final String ROOT = "http://10.0.2.2:5001/api/Equipo/";

    public static List<ArmasEntity> getArmas(List<String> s) {
        List<ArmasEntity> result = null;

        try {
            URL url = new URL(ROOT + "getMultiplesArmas");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", "Bearer " + Mochila.token);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            Gson gson = new Gson();
            String jsonInputString = gson.toJson(s);

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

                Type listType = new TypeToken<List<ArmasEntity>>() {
                }.getType();
                result = gson.fromJson(response.toString(), listType);

            } else {
                System.out.println("Error en la respuesta del servidor: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static List<ArmadurasEntity> getArmaduras(List<String> strings) {
        List<ArmadurasEntity> result = null;

        try {
            URL url = new URL(ROOT + "getMultiplesArmaduras");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", "Bearer " + Mochila.token);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            Gson gson = new Gson();
            String jsonInputString = gson.toJson(strings);

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

                Type listType = new TypeToken<List<ArmadurasEntity>>() {
                }.getType();
                result = gson.fromJson(response.toString(), listType);
            } else {
                System.out.println("Error en la respuesta del servidor: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
