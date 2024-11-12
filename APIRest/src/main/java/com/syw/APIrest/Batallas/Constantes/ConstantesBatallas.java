package com.syw.APIrest.Batallas.Constantes;

public class ConstantesBatallas {
    public static final int VIDA = 1000;
    public static final int DANO_BASE = 200;
    public static final int DANO_ALTO = 300;


    public static class MultiplicadoresDano {
        public static final double COMUN = 1.10;
        public static final double RARA = 1.20;
        public static final double ESPECIAL = 1.30;
        public static final double EPICA = 1.40;
        public static final double LEGANDARIA = 1.50;
    }

    public static class MultiplicadoresDefensa {
        public static final double COMUN = 0.90;
        public static final double RARA = 0.80;
        public static final double ESPECIAL = 0.70;
        public static final double EPICA = 0.60;
        public static final double LEGANDARIA = 0.50;
    }
}
