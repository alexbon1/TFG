package com.syw.APIrest.Accounts.Services;

import com.syw.APIrest.Accounts.Entitys.InventariosEntity;
import com.syw.APIrest.Stadistics.Entitys.ArmadurasEntity;
import com.syw.APIrest.Stadistics.Entitys.ArmasEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ServicioInventarios {

    @PersistenceContext
    EntityManager manager;


    public List<InventariosEntity> getFullList() {
        TypedQuery<InventariosEntity> consulta = manager.createQuery("select u from InventariosEntity u", InventariosEntity.class);
        return consulta.getResultList();
    }


    public InventariosEntity getByID(int id) {
        return manager.find(InventariosEntity.class, id);
    }

    @Transactional
    public boolean save(InventariosEntity inv) {
        try {
            if (inv != null) {
                manager.persist(inv);
                return true;
            } else {
                return false; // Indicates that the persistence failed
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Indicates that the persistence failed
        }
    }

    @Transactional
    public boolean deleteByID(int id) {
        try {
            InventariosEntity inv = getByID(id);
            if (inv != null) {
                manager.remove(inv);
                return true; // Indicates that the deletion was successful
            } else {
                return false; // Indicates that the user was not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Indicates that the deletion failed
        }
    }

    @Transactional
    public InventariosEntity modify(int id, String columna, String valor) {
        InventariosEntity inventario = getByID(id);
        if (inventario != null) {
            try {
                // Modificar la columna especificada con el nuevo valor.
                switch (columna.toLowerCase()) {
                    case "armaduras":
                        inventario.setArmaduras(valor);
                        break;
                    case "armas":
                        inventario.setArmas(valor);
                        break;
                    case "cartas":
                        inventario.setCartas(valor);
                        break;
                    case "setactual":
                        inventario.setSetActual(valor);
                        break;
                    case "personaje":
                        inventario.setPersonaje(valor);
                        break;
                    default:
                        return null;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return inventario;
            }
            // Persistir el inventario modificado.
            manager.persist(inventario);
            return inventario;
        } else {
            return null;
        }
    }

    public boolean comprobarExistencia(int id,Object cosa) {
        if (cosa instanceof ArmasEntity){
            String s = getByID(id).getArmas();
            if (s==null){
                s="";
            }
            String[] cosas=s.split("\\|");
            for (String sa : cosas) {
                if (sa.equals(((ArmasEntity) cosa).getId())){
                    return true;
                }
            }
        }else if(cosa instanceof ArmadurasEntity){
            String s = getByID(id).getArmaduras();
            if (s==null){
                s="";
            }
           String[] cosas= s.split("\\|");
            for (String sa : cosas) {
                if (sa.equals(((ArmadurasEntity) cosa).getId())){
                    return true;
                }
            }
        }
        return false;
    }
}
