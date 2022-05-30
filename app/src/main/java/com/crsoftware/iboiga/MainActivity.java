package com.crsoftware.iboiga;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ImageView imgLogo;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Vincular controles
        Vinculacion();

        //Ocultar ActionBar
        getSupportActionBar().hide();

        //Timer para mostrar la ventana un determinado tiempo
        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                //Marcar la activity a la que se tiene que ir una vez termine el splash
                Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intent);
                finish();
            }
        };

        Timer tiempo = new Timer();
        //En milisegundos el tiempo que queremos que aparezca
        tiempo.schedule(tarea,3500);

        //Girar Logo
        Animation animationScale = AnimationUtils.loadAnimation(this, R.anim.scale);
        imgLogo.startAnimation(animationScale);

        //Sonido logo
        mp = MediaPlayer.create(this,R.raw.logo_sound);
        mp.start();
    }

    private void Vinculacion() {
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
    }
}