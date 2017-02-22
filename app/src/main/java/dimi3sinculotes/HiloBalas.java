package dimi3sinculotes;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import dimi3sinculotes.Marciano;
import dimi3sinculotes.g6sprint2.R;

public class HiloBalas extends Thread {

    FullscreenActivity pantallaDelJuego;
    ImageView bala;
    Boolean dentro = true;

    public HiloBalas(FullscreenActivity ma, ImageView iv) {
        pantallaDelJuego = ma;
        bala = iv;


    }

    @Override
    public void run(){
        while (noEnd()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Toast.makeText(pantallaDelJuego, "ERROR en hilo de balas", Toast.LENGTH_SHORT).show();
            }
            pantallaDelJuego.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    ArrayList<Marciano> misMarcianos = pantallaDelJuego.getMarcianos();
                    Marciano aEliminar = null;
                    boolean end = false;
                    if (bala.getY() > 0) {
                        bala.setY(bala.getY() - 3);

                        for(Marciano marciano: misMarcianos){
                            int xf, yf;
                            xf = marciano.getX() + pantallaDelJuego.getTAMANO_MARCIANO();
                            yf = marciano.getY() + pantallaDelJuego.getTAMANO_MARCIANO();
                            if(choca(marciano.getX(), marciano.getY(), xf, yf)){
                                terminar();
                                FrameLayout layout = (FrameLayout) pantallaDelJuego.findViewById(R.id.activity_main);
                                layout.removeView(marciano.getIv());
                                 pantallaDelJuego.sumarPuntuacion();

                                aEliminar = marciano;
                                end = true;
                            }
                        }
                        if(end) {
                            misMarcianos.remove(aEliminar);
                            if(misMarcianos.isEmpty()){
                                pantallaDelJuego.crearMarcianos();
                            }
                        }
                    } else {
                        terminar();
                    }
                }
            });
        }
        pantallaDelJuego.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                terminar();
            }
        });
        pantallaDelJuego.restarBala();
    }
    private boolean noEnd(){
        return dentro && !pantallaDelJuego.isGameOver();
    }
    private void terminar(){
        FrameLayout layout = (FrameLayout) pantallaDelJuego.findViewById(R.id.activity_main);
        layout.removeView(bala);
        dentro = false;
    }
    private boolean choca(int x, int y, int xf, int yf){
        boolean toRet = false;
        if((x < bala.getX()) && (xf > bala.getX()) && (yf > bala.getY()) && (y < bala.getY())){
            toRet = true;
        }
        return toRet;
    }


}



