package dimi3sinculotes;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import org.w3c.dom.Text;

import dimi3sinculotes.g6sprint2.R;


public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_over);

        ImageView reiniciar = (ImageView) findViewById(R.id.reiniciar);
        int puntuacion = getIntent().getExtras().getInt("puntos");
        TextView textPuntuacion = (TextView) findViewById(R.id.puntuacionGameOver);
        textPuntuacion.setText("Puntuación: " + puntuacion);
        reiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
