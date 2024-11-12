package com.utad.android.storage;

import android.content.Context;
import com.google.gson.Gson;
import com.utad.android.entitys.UsersEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class UtilidadesJSON {

    public static void GuardarUser(Context context, UsersEntity user) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", user.getId());
            jsonObject.put("username", user.getUsername());
            jsonObject.put("nombre", user.getNombre());
            jsonObject.put("apellidos", user.getApellidos());
            jsonObject.put("fechaDeNacimiento", user.getFechaDeNacimiento());
            jsonObject.put("confirmada", user.getConfirmada());
            jsonObject.put("pwd", user.getPwd());
            jsonObject.put("nivel", user.getNivel());
            jsonObject.put("cantidadMisiones", user.getCantidadMisiones());
            jsonObject.put("monedas", user.getMonedas());
            jsonObject.put("email", user.getEmail());
            String jsonString = jsonObject.toString();

            // Obtener la ruta del directorio de archivos de la aplicación dentro del paquete de almacenamiento
            File directory = new File(context.getFilesDir(), "com/utad/android/storage");
            // Crear el directorio si no existe
            if (!directory.exists()) {
                directory.mkdirs();
            }
            // Crear el archivo en el directorio
            File file = new File(directory, "user.json");
            // Escribir la cadena JSON en el archivo
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonString);
            fileWriter.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public static UsersEntity obtenerUser(Context context) {
        UsersEntity user = null;
        try {
            // Obtener la ruta del archivo dentro del paquete de almacenamiento
            File file = new File(context.getFilesDir(), "com/utad/android/storage/user.json");
            // Verificar si el archivo existe
            if (file.exists()) {
                // Leer el contenido del archivo
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                StringBuilder jsonString = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    jsonString.append(line);
                }
                bufferedReader.close();

                Gson gson = new Gson();

                user = gson.fromJson(jsonString.toString(), UsersEntity.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

//    private static String convertirStringADate(String fechaStr) {
//        String fechaFormateada = null;
//        try {
//            // Formato para la fecha original
//            SimpleDateFormat formatoOriginal = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", new Locale("es", "ES"));
//            java.util.Date utilDate = formatoOriginal.parse(fechaStr);
//            // Convertir a java.sql.Date
//            Date fecha = new Date(utilDate.getTime());
//
//            // Formatear la fecha a un nuevo formato
//            SimpleDateFormat formatoNuevo = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//            fechaFormateada = formatoNuevo.format(fecha);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return fechaFormateada;
//    }

    public static boolean isUsuarioAbierto(Context context) {
        try {
            // Obtener la ruta del archivo dentro del paquete de almacenamiento
            File file = new File(context.getFilesDir(), "com/utad/android/storage/user.json");
            // Verificar si el archivo existe y tiene contenido
            return file.exists() && file.length() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void borrarUser(Context context) {
        try {
            File file = new File(context.getFilesDir(), "com/utad/android/storage/user.json");
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
