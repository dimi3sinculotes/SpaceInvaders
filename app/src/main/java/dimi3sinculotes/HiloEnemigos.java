package dimi3sinculotes;

import android.widget.Toast;

public class HiloEnemigos extends Thread {

    FullscreenActivity fsa;

    public HiloEnemigos(FullscreenActivity fsa){
        this.fsa = fsa;
    }


    @Override
    public void run(){
        while (noEnd()) {
            try {
                Thread.sleep(2000 + ((int) (Math.random() * 3000)));
            } catch (InterruptedException ex) {
                Toast.makeText(fsa, "ERROR en hilo de enemigos", Toast.LENGTH_SHORT).show();
            }
            if(noEnd()) {
                fsa.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        fsa.dropearBalaMarciano();
                    }
                });
            }
        }
    }
    private boolean noEnd(){
        return !fsa.isGameOver();
    }
}
