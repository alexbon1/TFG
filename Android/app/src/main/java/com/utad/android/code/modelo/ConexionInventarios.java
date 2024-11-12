package com.utad.android.code.modelo;

import com.google.gson.Gson;
import com.utad.android.entitys.InventariosEntity;
import com.utad.android.storage.Mochila;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConexionInventarios {

    private static final String ROOT = "http://10.0.2.2:5001/api/Inventarios/";

    public static InventariosEntity modifyInventario(int id, String columna, String valor) {
        InventariosEntity result = null;

        try {
            URL apiUrl = new URL(ROOT + "modify");
            HttpURLConnection urlConnection = (HttpURLConnection) apiUrl.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", "Bearer " + Mochila.token);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            Gson gson = new Gson();
            String jsonBody = "{\"id\":" + id + ", \"columna\":\"" + columna + "\", \"valor\":\"" + valor + "\"}";

            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
            writer.write(jsonBody);
            writer.flush();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                result = gson.fromJson(response.toString(), InventariosEntity.class);
            } else {
                System.out.println("Error en la respuesta del servidor: " + urlConnection.getResponseCode());
            }

            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static InventariosEntity getInventarioByID(int id) {
        InventariosEntity inventario = new InventariosEntity(id);

        try {
            URL apiUrl = new URL(ROOT + "getByID");
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + Mochila.token);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            Gson gson = new Gson();

            try (OutputStream os = connection.getOutputStream()) {
                String jsonBody = gson.toJson(inventario);
                byte[] input = jsonBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                inventario = gson.fromJson(response.toString(), InventariosEntity.class);

            } else {
                System.out.println("Error en la respuesta del servidor: " + connection.getResponseCode());
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inventario;
    }
}

