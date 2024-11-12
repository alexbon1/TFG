package com.utad.android.code.modelo;

import com.google.gson.Gson;
import com.utad.android.entitys.JSON.BoxJSON;
import com.utad.android.entitys.JSON.CompraJSON;
import com.utad.android.entitys.JSON.TiendaDiaJSON;
import com.utad.android.entitys.ArmasEntity;
import com.utad.android.entitys.ArmadurasEntity;
import com.utad.android.entitys.InventariosEntity;
import com.utad.android.storage.Mochila;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ConexionTienda {

    private static final String ROOT = "http://10.0.2.2:5001/api/Tienda/";

    public static TiendaDiaJSON getTiendaDiaria() {
        TiendaDiaJSON result = null;

        try {
            URL url = new URL(ROOT + "TiendaDiaria");
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

                result = gson.fromJson(response.toString(), TiendaDiaJSON.class);
            } else {
                System.out.println("Error en la respuesta del servidor: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static InventariosEntity comprar(Object compra) {
        InventariosEntity inventario = null;
        CompraJSON cmp = null;
        if (compra instanceof ArmasEntity) {
            cmp = new CompraJSON(Mochila.user, (ArmasEntity) compra);
        } else if (compra instanceof ArmadurasEntity) {
            cmp = new CompraJSON(Mochila.user, (ArmadurasEntity) compra);
        }
        try {
            URL apiUrl = new URL(ROOT + "compra");
            HttpURLConnection urlConnection = (HttpURLConnection) apiUrl.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", "Bearer " + Mochila.token);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);
            Gson gson = new Gson();

            try (OutputStream os = urlConnection.getOutputStream()) {
                String jsonBody = gson.toJson(cmp);
                byte[] input = jsonBody.getBytes("utf-8");
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

                inventario = gson.fromJson(response.toString(), InventariosEntity.class);

            } else {
                System.out.println("Error en la respuesta del servidor: " + urlConnection.getResponseCode());
            }

            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inventario;
    }

    public static BoxJSON comprarBoxBarata() {
        BoxJSON result = null;

        try {
            URL url = new URL(ROOT + "compra/boxBarata");
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

                result = gson.fromJson(response.toString(), BoxJSON.class);
            } else {
                System.out.println("Error en la respuesta del servidor: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static BoxJSON comprarBoxCara() {
        BoxJSON result = null;

        try {
            URL url = new URL(ROOT + "compra/boxCara");
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

                result = gson.fromJson(response.toString(), BoxJSON.class);
            } else {
                System.out.println("Error en la respuesta del servidor: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
