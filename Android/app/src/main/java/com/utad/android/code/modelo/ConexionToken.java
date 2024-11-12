package com.utad.android.code.modelo;

import com.google.gson.Gson;
import com.utad.android.storage.Mochila;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ConexionToken {

    private static final String TOKEN_URL = "http://10.0.2.2:5001/api/auth/token";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "pwd1234";

    public static void solicitarToken() {
        try {
            URL url = new URL(TOKEN_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            // Create JSON object with username and password
            JSONObject jsonInput = new JSONObject();
            jsonInput.put("username", USERNAME);
            jsonInput.put("password", PASSWORD);

            try (OutputStream os = urlConnection.getOutputStream()) {
                byte[] input = jsonInput.toString().getBytes(StandardCharsets.UTF_8);
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

                // Assuming the response contains the token as plain text
                String token = response.toString();
                Mochila.token = token;
            } else {
                System.out.println("Error en la respuesta del servidor: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
