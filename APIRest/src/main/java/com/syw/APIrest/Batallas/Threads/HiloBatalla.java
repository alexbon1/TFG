package com.syw.APIrest.Batallas.Threads;

import com.syw.APIrest.Batallas.Operations.ReceptorPrincipalBatallas;

public class HiloBatalla extends Thread{
    private final ReceptorPrincipalBatallas receptorPrincipalBatallas;

    public HiloBatalla(ReceptorPrincipalBatallas receptorPrincipalBatallas) {
        this.receptorPrincipalBatallas = receptorPrincipalBatallas;
    }

    @Override
    public void run() {
        receptorPrincipalBatallas.run();
    }

    public ReceptorPrincipalBatallas getBatalla() {
        return receptorPrincipalBatallas;
    }
}
