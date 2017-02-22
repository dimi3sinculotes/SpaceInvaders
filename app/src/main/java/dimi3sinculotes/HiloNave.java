package dimi3sinculotes;


import android.widget.Toast;

import dimi3sinculotes.FullscreenActivity;

public class HiloNave extends Thread {

    FullscreenActivity pantallaDelJuego;

    public HiloNave(FullscreenActivity ma){
        pantallaDelJuego = ma;
    }

    @Override
    public void run() {
        while (noEnd()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Toast.makeText(pantallaDelJuego, "ERROR en hilo de movimiento lateral", Toast.LENGTH_SHORT).show();
            }
            pantallaDelJuego.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    pantallaDelJuego.mover();
                }
            });
        }
    }
    private boolean noEnd(){
        return !pantallaDelJuego.isGameOver();
    }
}

