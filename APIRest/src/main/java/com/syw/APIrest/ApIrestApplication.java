package com.syw.APIrest;


import com.syw.APIrest.Accounts.Entitys.InventariosEntity;
import com.syw.APIrest.Accounts.Entitys.UsersEntity;
import com.syw.APIrest.Accounts.Services.ServicioInventarios;
import com.syw.APIrest.Accounts.Services.ServicioUsuarios;
import com.syw.APIrest.Seguridad.EncryptionUtil;
import com.syw.APIrest.Stadistics.Entitys.ArmadurasEntity;
import com.syw.APIrest.Stadistics.Entitys.ArmasEntity;
import com.syw.APIrest.Stadistics.Services.ServicioArmaduras;
import com.syw.APIrest.Stadistics.Services.ServicioArmas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

@SpringBootApplication
public class ApIrestApplication implements CommandLineRunner {
    @Autowired
    ServicioArmaduras servicioar;
    @Autowired
    ServicioArmas servicioa;
    @Autowired
    ServicioUsuarios servicioi;
    public static void main(String[] args) {
        SpringApplication.run(ApIrestApplication.class, args);
    }

    public void run(String... args) throws Exception {
    }




    //        List<String>a= new LinkedList<>();
//        for (ArmadurasEntity armaduras : servicioar.getFullList()) {
//            if (a.contains(armaduras.getNombre())){
//                servicioar.delete(armaduras.getId());
//
//            }else {
//                a.add(armaduras.getNombre());
//            }
//        }
//        String[]a= {"Rara","Legendaria","Especial","Epica"};
//        for (ArmadurasEntity armaduras : servicioar.getFullList()) {
//            for (String s : a) {
//                ArmadurasEntity n = new ArmadurasEntity(armaduras.getNombre(),s,armaduras.getImagen(),armaduras.getStats(),armaduras.getDescripcion());
//                servicioar.save(n);
//            }
//        }
}



