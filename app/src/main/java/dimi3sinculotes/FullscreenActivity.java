package dimi3sinculotes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import dimi3sinculotes.g6sprint2.R;

public class FullscreenActivity extends AppCompatActivity{

    private static final boolean AUTO_HIDE = true;

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mp!=null) {
            mp.stop();
        }
        setGameOver(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mp!=null) {
            mp.stop();
        }
        setGameOver(true);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fullscreen);


        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
// CODIGO NUESTRO ----------- HASTA FINAL DE onCreate  ------------------------------------------------------------
        // Creamos un objeto de clase HiloNave e iniciamos su Thread, se trata del hilo encargado del
        // movimiento izda/dcha de Nuestra nave protagonista

                sonido = SpaceInvaders.bsonido;
                marcianos = new ArrayList<Marciano>();
                Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                TAMANO_MARCIANO = (size.x/10);

                HiloNave hn = new HiloNave(this);
                hn.start();
        // Ahora inicializamos los botones (direcciones, disparar y mute/unmute y les seteamos sus
        // correspondientes acciones

        crearMarcianos();


                Button bLeft = (Button) findViewById(R.id.btnLeft);
                Button bRight = (Button) findViewById(R.id.btnRight);
                Button bFire = (Button) findViewById(R.id.btnFire);
                ImageView letrero = (ImageView)findViewById(R.id.gameOver);
                Button reiniciar = (Button)findViewById(R.id.botonReinicio);

                bLeft.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                            pararNave();
                        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            moverIzda();
                        }
                        return false;
                    }
                });

                bRight.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                            pararNave();
                        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            moverDcha();
                        }
                        return false;
                    }
                });

                bFire.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                            // No hacer nada
                        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            disparar();
                        }
                        return false;
                    }
                });

        // Y por ultimo encontramos la imagen (ImageView) de nuestra nave en la interfaz y la
        // guardamos en "nave". La posicionamos en el medio de la pantalla
                ImageView nave = (ImageView)findViewById(R.id.nave);
                nave.setX(dimensionesPantalla('x')/2);

        HiloEnemigos he = new HiloEnemigos(this);
        he.start();

        letrero.setVisibility(View.GONE);
        reiniciar.setVisibility(View.GONE);
        sp = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        kill= sp.load(this,R.raw.kill,1);
        disparo= sp.load(this,R.raw.shot,1);
        bgMusic();
    }


    // ---------------------- MAS CODIGO AUTOMATICO CORRESPONDIENTE AL FULLSCREEN DE ANDROIDSTUDIO --------------------------------
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
            hide();
    }


    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);

        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    // CODIGO NUESTRO -------------------------------------------------------------------------------
    // vars y constantes
    private final int MARGEN_PANTALLA = 40; // Al poner la nave en x=0 en la pantalla se "cortaba", esto resulta ser el margen minimo
    private final int NUM_BALAS = 12; // El numero de balas que puede haber en pantalla sin que se sobrecaliente la nave
    private int direccion = 0; // esta variable {-1,0,1} determina a que lado y si se mueve la nave con HiloNave
    private int balas = 0; // Cuantas balas se encuentran en pantalla en cada momento
    public boolean retardo = true; // true = puedes disparar / false = no. Para simular unas rafagas mas realistas
    private boolean sonido = true; // true = unmute / false = mute;
    private ArrayList<Marciano> marcianos;
    private int TAMANO_MARCIANO = 60;
    private int puntuacion = 0;
    private int vidas = 3;
    private boolean isGameOver = false;
    private int currentSong = 5;
    private MovimientoMarcianos mov;
    private int velocidad = 50;
    MediaPlayer mp;
    int kill, disparo;
    SoundPool sp;

    // metodos

    private void bgMusic(){
        if(sonido && !isGameOver){
            int aux = currentSong;
            while(aux == currentSong) {
                currentSong = (int) (Math.random() * 2);
            }
            playSong(currentSong);
        }
    }

    private void playSong(int i){
        int song = R.raw.song1;
        switch(i){
            case 0:
                song = R.raw.song1;
                break;
            case 1:
                song = R.raw.song2;
                break;
        }

        mp = MediaPlayer.create(getApplicationContext(), song);
        mp.start();
        mp.setVolume(5,50);
        mp.setLooping(true);
    }

    public void dropearBalaMarciano(){
        ImageView nave = (ImageView) findViewById(R.id.nave); // sincroniza la imagen de la nave con nave (codigo)
        FrameLayout layoutPrincipal = (FrameLayout) findViewById(R.id.activity_main); // Entorno de juego
        ImageView nuevaBala = new ImageView(this); // Una bala blanca que va a disparar
        //Valor inicial 32

        Marciano villano = marcianos.get((int)(Math.random() * marcianos.size()));

        nuevaBala.setLayoutParams(new android.view.ViewGroup.LayoutParams(10, 15));
        nuevaBala.setMaxHeight(5);
        nuevaBala.setMaxWidth(3);
        nuevaBala.setX(villano.getMedio().x);
        nuevaBala.setY(villano.getMedio().y);
        nuevaBala.setBackgroundColor(Color.RED);

        layoutPrincipal.addView(nuevaBala);

        sonidoDisparo();


        HiloBalasEnemigas hb = new HiloBalasEnemigas(nave, nuevaBala, this);
        hb.start();

    }


    public void perderVida(){
        vidas--;
        ImageView v;
        switch(vidas) {
            case 2:
                v = (ImageView) findViewById(R.id.vida3);
                break;
            case 1:
                v = (ImageView) findViewById(R.id.vida2);
                break;
            default:
                v = (ImageView) findViewById(R.id.vida1);
                gameOver();
                break;
        }

        v.setImageResource(R.drawable.novida);
    }

    public void gameOver(){
        Intent intent = new Intent(getApplicationContext(), GameOverActivity.class);
        intent.putExtra("puntos", puntuacion);
        startActivity(intent);
        isGameOver = true;
        if(mp!=null){
            mp.stop();
        }

        finish();
    }

    public void crearMarcianos() {
        if(mov != null) {
            mov.parar();
            if(velocidad>30) {
                velocidad = velocidad - 10;
            } else if(velocidad>15){
                velocidad = velocidad -5;
            }
        }
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 7; j++) {
                ImageView iv1 = new ImageView(this);
                if((i == 1) || (i == 3)) {
                    iv1.setImageResource(R.drawable.bicho1);
                }else if((i == 2) || (i == 4)){
                    iv1.setImageResource(R.drawable.bicho2);
                }else{
                    iv1.setImageResource(R.drawable.bicho3);
                }
                iv1.setLayoutParams(new android.view.ViewGroup.LayoutParams(TAMANO_MARCIANO, TAMANO_MARCIANO));

                iv1.setX(30 + size.x/9 * j);
                iv1.setY(100 + size.y/15 * i);

                FrameLayout layoutPrincipal = (FrameLayout) findViewById(R.id.activity_main);
                layoutPrincipal.addView(iv1);

                Marciano m1 = new Marciano((int)(iv1.getX()), (int)(iv1.getY()), j, iv1);
                marcianos.add(m1);
            }
        }
        ImageView nave = (ImageView) findViewById(R.id.nave);
        mov = new MovimientoMarcianos(this, marcianos, size.x, nave);
        mov.start();
    }

    private void disparar() {
        if((balas < NUM_BALAS) && (retardo)) {
            ImageView nave = (ImageView) findViewById(R.id.nave); // sincroniza la imagen de la nave con nave (codigo)
            FrameLayout layoutPrincipal = (FrameLayout) findViewById(R.id.activity_main); // Entorno de juego
            ImageView nuevaBala = new ImageView(this); // Una bala blanca que va a disparar
            //Valor inicial 32

            int mitadNave = (int)(nave.getWidth() / 2.2);
            int spawnx = (int) (nave.getX() + mitadNave); // posicion ajustada desde donde saldra la bala: X
            int spawny = (int) (nave.getY() - 15 ); // e Y

            nuevaBala.setLayoutParams(new android.view.ViewGroup.LayoutParams(10, 15));
            nuevaBala.setMaxHeight(5);
            nuevaBala.setMaxWidth(3);
            nuevaBala.setX(spawnx);
            nuevaBala.setY(spawny);
            nuevaBala.setBackgroundColor(Color.WHITE);

            layoutPrincipal.addView(nuevaBala);

            sonidoDisparo();
            balas++;

            Retardo r = new Retardo();
            r.start();

            HiloBalas hb = new HiloBalas(this, nuevaBala);
            hb.start();
        }
    }

    private void moverIzda() {
        direccion = -1;
    }

    private void moverDcha() {
        direccion = 1;
    }

    private void pararNave() {
        direccion = 0;
    }

    public void mover() {
        int anchoPantalla = dimensionesPantalla('x');
        ImageView nave = (ImageView) findViewById(R.id.nave);
        boolean cercaDelMargen = (nave.getX() + 1 * direccion <= anchoPantalla - nave.getWidth() - MARGEN_PANTALLA) && (nave.getX() + 1 * direccion >= MARGEN_PANTALLA);
        boolean dentroDePantalla = ((nave.getX() + 7 * direccion <= anchoPantalla - nave.getWidth() - MARGEN_PANTALLA) && (nave.getX() + 7 * direccion >= MARGEN_PANTALLA));

        if (dentroDePantalla) {
            nave.setX(nave.getX() + 7 * direccion);
        } else if (cercaDelMargen) {
            nave.setX(nave.getX() + direccion);
        }
    }

    public int dimensionesPantalla(char c){
        int respuesta = 0;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        if(c == 'x'){
            respuesta = size.x;
        }else if(c == 'y'){
            respuesta = size.y;
        }

        return respuesta;
    }

    public void restarBala(){
        balas--;
    }

    private void sonidoDisparo(){
        if(sonido){
            sp.play(disparo, 1, 1, 0, 0, 1);
            /*
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.shot);
            mp.start();
            */
        }
    }

    class Retardo extends Thread{
        @Override
        public void run(){
            retardo = false;
            try {
                sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retardo = true;
        }
    }

    public ArrayList<Marciano> getMarcianos() {
        return marcianos;
    }

    public int getTAMANO_MARCIANO() {
        return TAMANO_MARCIANO;
    }
    public void sumarPuntuacion(){
        puntuacion++;
        TextView tv = (TextView)findViewById(R.id.textView);
        tv.setText(String.format("PUNTUACION: " + "%04d",puntuacion));
        if(sonido){
            sp.play(this.kill, 1, 1, 0, 0, 1);
            /**
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.kill);
            mp.start();
             */
        }
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public int getVelocidad(){
        return velocidad;
    }


}

