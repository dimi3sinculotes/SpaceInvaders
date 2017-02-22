package dimi3sinculotes;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import dimi3sinculotes.g6sprint2.R;

public class MovimientoMarcianos extends Thread {
    FullscreenActivity pantallaDelJuego;
    ArrayList<Marciano> marcianos;
    static int direccion = 5;
    int tamano;
    int contador = 0;
    boolean noStop = true;
    ImageView nave;

    public MovimientoMarcianos(FullscreenActivity ma, ArrayList<Marciano> m, int tamano, ImageView nave){
        pantallaDelJuego = ma;
        marcianos = m;
        this.tamano = tamano;
        this.nave = nave;
    }

    @Override
    public void run() {

        while(noEnd()){
            try {
                Thread.sleep(pantallaDelJuego.getVelocidad());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(noEnd()) {
                pantallaDelJuego.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(Marciano marciano: marcianos){
                            marciano.getIv().setX(marciano.getIv().getX() + direccion);
                            marciano.setX((int) marciano.getIv().getX());
                        }
                        for (Marciano marciano : marcianos) {

                            if (direccion >0 && marciano.getX() + marciano.getIv().getWidth() > tamano - 10) {
                                MovimientoMarcianos.setDireccion();
                                if(contador>=2) {
                                    for (Marciano marciano2 : marcianos) {
                                        marciano2.getIv().setY(marciano2.getIv().getY() + marciano2.getIv().getHeight() / 2);
                                        marciano2.setY((int) marciano2.getIv().getY());
                                    }
                                    for (Marciano marciano3 : marcianos) {
                                        if (marciano3.getY() + marciano3.getIv().getHeight() > nave.getY()) {
                                            pantallaDelJuego.gameOver();
                                        }
                                    }
                                    contador=0;
                                } else {
                                    contador++;
                                }
                            } else if (direccion < 0 && marciano.getX() < 10) {
                                MovimientoMarcianos.setDireccion();
                                if(contador>=2) {
                                    for (Marciano marciano2 : marcianos) {
                                        marciano2.getIv().setY(marciano2.getIv().getY() + marciano2.getIv().getHeight() / 2);
                                        marciano2.setY((int) marciano2.getIv().getY());
                                    }
                                    for (Marciano marciano3 : marcianos) {
                                        if (marciano3.getY() + marciano3.getIv().getHeight() > nave.getY()) {
                                            pantallaDelJuego.gameOver();
                                        }
                                    }
                                    contador = 0;
                                } else {
                                    contador++;
                                }
                            }


                        }
                    }
                });
            }
        }
        if(pantallaDelJuego.isGameOver()){
            pantallaDelJuego.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (Marciano marciano : marcianos) {
                        FrameLayout layout = (FrameLayout) pantallaDelJuego.findViewById(R.id.activity_main);
                        layout.removeView(marciano.getIv());
                    }
                }
            });
        }
    }

    private boolean noEnd(){
        return !pantallaDelJuego.isGameOver() && noStop;
    }

    public static void setDireccion(){
        direccion = direccion * -1;
    }

    public void parar() {
        noStop = false;
        pantallaDelJuego.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Marciano marciano : marcianos) {
                    FrameLayout layout = (FrameLayout) pantallaDelJuego.findViewById(R.id.activity_main);
                    layout.removeView(marciano.getIv());
                }
            }
        });
    }

}
