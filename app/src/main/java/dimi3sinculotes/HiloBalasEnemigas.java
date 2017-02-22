package dimi3sinculotes;

import android.graphics.Point;
import android.view.Display;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import dimi3sinculotes.g6sprint2.R;

public class HiloBalasEnemigas extends Thread {

    ImageView nave, bala;
    FullscreenActivity pantallaDelJuego;
    boolean dentro = true;
    int n;
    boolean end = false;

    public HiloBalasEnemigas (ImageView nave, ImageView bala, FullscreenActivity main){
        this.nave = nave;
        this.pantallaDelJuego = main;
        this.bala = bala;
    }

    @Override
    public void run(){
        while (noEnd()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Toast.makeText(pantallaDelJuego, "ERROR en hilo de balas marcianas", Toast.LENGTH_SHORT).show();
            }
            pantallaDelJuego.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if((nave.getX() < bala.getX()) && (nave.getX()+nave.getWidth() > bala.getX()) && (nave.getY() < bala.getY()) && (nave.getY()+nave.getWidth() > bala.getY()) && !end) {
                        end = true;
                        pantallaDelJuego.perderVida();
                        terminar();
                    }

                    if ((bala.getY() < nave.getY()+nave.getHeight()+10) && (!end)){
                        bala.setY(bala.getY() + 3);
                    } else {
                        terminar();
                    }
                }
            });
        }
    }

    private boolean noEnd(){
        return dentro && !pantallaDelJuego.isGameOver();
    }
    private void terminar(){
        FrameLayout layout = (FrameLayout) pantallaDelJuego.findViewById(R.id.activity_main);
        layout.removeView(bala);
        dentro = false;
    }
}
