package com.crsoftware.imotiva;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



public class MainActivity2 extends AppCompatActivity {

    ArrayList<String> motiva = new ArrayList<>();

    TextView txtMensaje;
    ImageView imgLogoInterior;
    Button btRecargar;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy");
    int numAleatorio;
    String fechaActual;
    SharedPreferences pref;
    Animation animationScale;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Dejar la pantalla en vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        pref = getSharedPreferences("configuracion", Context.MODE_PRIVATE);

        //Ocultar ActionBar
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        //Vincular controles
        Vinculacion();

        //Girar Logo y boton
        animationScale = AnimationUtils.loadAnimation(this, R.anim.scale);
        imgLogoInterior.startAnimation(animationScale);


        //Cargar fichero de motivaciones
        CargarMotivaciones();

        //ComprobarConfiguracion
        ComprobarConfiguracion();
    }

    private void ComprobarConfiguracion() {
        //Obtener fecha
        fechaActual = ObtenerFecha();

        //Comprobar que no haya repetido día
        String msg = pref.getString(fechaActual,"");

        if (msg.length() < 1)
        {
            //Mostrar en control
            MostrarMotivacion(motiva);

            //Mostrar boton si es la primera vez
            btRecargar.setVisibility(View.VISIBLE);
            btRecargar.startAnimation(animationScale);


            //Guardar en fichero
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(fechaActual,txtMensaje.getText().toString());
            editor.apply();
        }
        else{
            txtMensaje.setText(msg);
            btRecargar.setVisibility(View.INVISIBLE);
        }
    }

    private String ObtenerFecha() {
        Calendar calendar = Calendar.getInstance();
        Date dateObj = calendar.getTime();
        return dtf.format(dateObj);
    }

    private void CargarMotivaciones() {
        try {
            //Abrir el fichero
            InputStreamReader archivo = new InputStreamReader(getResources().openRawResource(R.raw.motivadoras));
            //Leer el fichero
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();

            //Leer el fichero completo y acumularlo
            while(linea != null){
                motiva.add(linea);
                linea = br.readLine();
            }

            //Dejar de leer y cerrar el fichero
            br.close();
            archivo.close();

        }catch (Exception e){
            Toast.makeText(this, R.string.strErrorFichero, Toast.LENGTH_SHORT).show();
        }
    }

    private void MostrarMotivacion(ArrayList<String> motiva) {
        String mensaje;

        if (motiva.size() > 0) {
            //Contar el nº de lineas y elegir una aleatoriamente
            numAleatorio = (int) (Math.random() * motiva.size() + 1);

            mensaje = motiva.get(numAleatorio);
            txtMensaje.setText(mensaje.toUpperCase());
        }
        else{
            Toast.makeText(this, R.string.strErrorFichero, Toast.LENGTH_SHORT).show();
        }
    }

    private void Vinculacion() {
        txtMensaje = findViewById(R.id.txtMensaje);
        imgLogoInterior = findViewById(R.id.imgLogoInterior);
        btRecargar = findViewById(R.id.btRepetir);

        btRecargar.setOnClickListener(this::Recargar);
    }

    private void Recargar(View view) {
        int aleatorio =  (int) (Math.random() * motiva.size() + 1);
        String mensajeBis = motiva.get(aleatorio);

        txtMensaje.setText(mensajeBis.toUpperCase());
        btRecargar.setVisibility(View.INVISIBLE);

        //Guardar en fichero
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(fechaActual,txtMensaje.getText().toString());
        editor.apply();
    }
}