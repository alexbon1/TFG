package com.syw.APIrest.Accounts.Services;

import com.syw.APIrest.Accounts.Entitys.UsersEntity;
import com.syw.APIrest.Seguridad.EncryptionUtil;
import com.syw.APIrest.Stadistics.Constantes.ConstantesXP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.util.List;

@Repository
public class ServicioUsuarios {

    @PersistenceContext
    EntityManager manager;
    @Autowired
    private EncryptionUtil EncryptionUtil;
    public List<UsersEntity> getFullList() {
        TypedQuery<UsersEntity> consulta = manager.createQuery("select u from UsersEntity u", UsersEntity.class);
        return consulta.getResultList();
    }


    public UsersEntity getByID(int id) {
        return manager.find(UsersEntity.class, id);
    }

    public UsersEntity getByUsername(String username) {
        TypedQuery<UsersEntity> consulta = manager.createQuery("SELECT u FROM UsersEntity u WHERE u.username = :username", UsersEntity.class);
        consulta.setParameter("username", username);
        try {
            UsersEntity user = consulta.getSingleResult();
            String decryptedPwd = EncryptionUtil.decrypt(user.getPwd());
            user.setPwd(decryptedPwd);
            return user;
        } catch (NoResultException e) {
            // Manejar el caso cuando no se encuentra ningún usuario con el nombre de usuario dado
            return null;
        } catch (Exception e) {
            // Manejar la excepción de desencriptación
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public boolean save(UsersEntity user) {
        try {
            if (user != null) {
                String encryptedPassword = EncryptionUtil.encrypt(user.getPwd());
                user.setPwd(encryptedPassword);
                manager.persist(user);
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
            UsersEntity user = getByID(id);
            if (user != null) {
                manager.remove(user);
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
    public UsersEntity modify(int id, String columna, String valor) {
        UsersEntity user = getByID(id);
        if (user != null) {
            try {
                // Modify the specified column with the new value.
                switch (columna.toLowerCase()) {
                    case "nombre":
                        user.setNombre(valor);
                        break;
                    case "email":
                        user.setEmail(valor);
                        break;
                    case "apellidos":
                        user.setApellidos(valor);
                        break;
                    case "fechadenacimiento":
                        user.setFechaDeNacimiento(Date.valueOf(valor));
                        break;
                    case "confirmada":
                        user.setConfirmada(Byte.parseByte(valor));
                        break;
                    case "pwd":
                        user.setPwd(valor);
                        break;
                    case "tipo":
                        user.setTipo(valor);
                        break;
                    case "monedas":
                        user.setMonedas(Integer.parseInt(valor));
                        break;
                    case "username":
                        user.setUsername(valor);
                        break;
                    case "nivel":
                        user.setNivel(Integer.parseInt(valor));
                        break;
                    case "misiones":
                        user.setCantidadMisiones(Integer.parseInt(valor));
                        break;
                    default:
                        return null;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return null;
            }
            // Persist the modified user.
            manager.persist(user);
            return user;
        } else {
            return null;
        }
    }
    private static int[] xpAcumuladoPorNivel;

    static {
        xpAcumuladoPorNivel = generarTablaNiveles();
    }

    private static int[] generarTablaNiveles() {
        int[] niveles = new int[101];
        int xpAcumulado = 0;

        for (int i = 1; i <= 100; i++) {
            if (i <= 10) {
                xpAcumulado += ConstantesXP.Niveles.NIVEL0_10;
            } else if (i <= 20) {
                xpAcumulado += ConstantesXP.Niveles.NIVEL10_20;
            } else if (i <= 30) {
                xpAcumulado += ConstantesXP.Niveles.NIVEL20_30;
            } else if (i <= 40) {
                xpAcumulado += ConstantesXP.Niveles.NIVEL30_40;
            } else if (i <= 50) {
                xpAcumulado += ConstantesXP.Niveles.NIVEL40_50;
            } else if (i <= 60) {
                xpAcumulado += ConstantesXP.Niveles.NIVEL50_60;
            } else if (i <= 70) {
                xpAcumulado += ConstantesXP.Niveles.NIVEL60_70;
            } else if (i <= 80) {
                xpAcumulado += ConstantesXP.Niveles.NIVEL70_80;
            } else if (i <= 90) {
                xpAcumulado += ConstantesXP.Niveles.NIVEL80_90;
            } else {
                xpAcumulado +=ConstantesXP.Niveles.NIVEL90_100;
            }
            niveles[i] = xpAcumulado;
        }

        return niveles;
    }

    public static int obtenerNivel(int xpActual) {
        for (int i = 1; i < xpAcumuladoPorNivel.length; i++) {
            if (xpActual < xpAcumuladoPorNivel[i]) {
                return i - 1;
            }
        }
        return 100;
    }

    public static int xpParaSiguienteNivel(int xpActual) {
        int nivelActual = obtenerNivel(xpActual);
        if (nivelActual == 100) {
            return 0; // Ya está en el nivel máximo
        }
        return xpAcumuladoPorNivel[nivelActual + 1] - xpActual;
    }
    public static int obtenerRangoXP(int nivel) {
        if (nivel <= 10) {
            return ConstantesXP.Niveles.NIVEL0_10;
        } else if (nivel <= 20) {
            return ConstantesXP.Niveles.NIVEL10_20;
        } else if (nivel <= 30) {
            return ConstantesXP.Niveles.NIVEL20_30;
        } else if (nivel <= 40) {
            return ConstantesXP.Niveles.NIVEL30_40;
        } else if (nivel <= 50) {
            return ConstantesXP.Niveles.NIVEL40_50;
        } else if (nivel <= 60) {
            return ConstantesXP.Niveles.NIVEL50_60;
        } else if (nivel <= 70) {
            return ConstantesXP.Niveles.NIVEL60_70;
        } else if (nivel <= 80) {
            return ConstantesXP.Niveles.NIVEL70_80;
        } else if (nivel <= 90) {
            return ConstantesXP.Niveles.NIVEL80_90;
        } else {
            return ConstantesXP.Niveles.NIVEL90_100;
        }
    }
}
