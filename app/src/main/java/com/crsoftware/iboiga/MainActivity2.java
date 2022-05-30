package com.crsoftware.iboiga;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity2 extends AppCompatActivity {

    AutoCompleteTextView txtLista;
    TextView txtResultado,txtPorcentaje;
    Button btCapturar;
    private ImageView imgLogoInterior;
    ObjectAnimator animatorAlpha;
    ObjectAnimator animatorAlphaPor;
    final long animationDuration = 1000;
    final String[] ciudades = new String[52];
    MediaPlayer mp;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Dejar la pantalla en vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //Ocultar ActionBar
        if (getSupportActionBar() != null)
         getSupportActionBar().hide();

        Vinculacion();

        //Girar Logo
        Animation animationScale = AnimationUtils.loadAnimation(this, R.anim.scale);
        imgLogoInterior.startAnimation(animationScale);

        //Cargar fichero de ciudades
        CargarCiudades();
    }

    private void CargarCiudades() {
            try {
                //Abrir el fichero
                InputStreamReader archivo = new InputStreamReader(getResources().openRawResource(R.raw.ciudades));
                //Leer el fichero
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                int contador = 0;

                //Leer el fichero completo y acumularlo
                while(linea != null){
                    ciudades[contador] = linea;
                    linea = br.readLine();
                    contador++;
                }

                //Dejar de leer y cerrar el fichero
                br.close();
                archivo.close();

                //Cargar en control
                CargarListaEnControl(ciudades);

            }catch (Exception e){
                Toast.makeText(this, R.string.strErrorFichero, Toast.LENGTH_SHORT).show();
                Limpiar();
            }
    }

    private void CargarListaEnControl(String[] progLanguages) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, progLanguages);

        txtLista.setThreshold(1);
        txtLista.setAdapter(arrayAdapter);
    }

    private void Vinculacion() {
        txtLista = findViewById(R.id.txtLista);
        btCapturar = findViewById(R.id.btCapturar);
        txtResultado = findViewById(R.id.txtResultado);
        txtPorcentaje = findViewById(R.id.txtPorcentaje);
        imgLogoInterior = findViewById(R.id.imgLogoInterior);

        btCapturar.setOnClickListener(this::Capturar);

        txtLista.setOnClickListener(view -> txtLista.showDropDown());
    }

    public void Capturar (View view){
        String seleccion = txtLista.getText().toString();

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        boolean valorEncontrado = false;

        if (txtLista.getText() != null) {

            for (String sel : ciudades)
                if (seleccion.equalsIgnoreCase(sel)) {
                    valorEncontrado = true;
                    break;
                }

            if (valorEncontrado) {
                int numAleatorio = (int) (Math.random() * 8 + 1);
                //Mostrar resultado
                String mostrarResultado = getString(R.string.strResultado, seleccion);
                String porcen = Integer.toString(numAleatorio);
                EfectoResultado(mostrarResultado, porcen);
            }else{
                Toast.makeText(this, R.string.strFaltaCiudad, Toast.LENGTH_SHORT).show();
                Limpiar();
            }


        }else{
            Toast.makeText(this, R.string.strFaltaCiudad, Toast.LENGTH_SHORT).show();
            Limpiar();
        }
    }

    private void EfectoResultado(String mostrarResultado, String porcen) {
        txtResultado.setText(mostrarResultado);
        txtPorcentaje.setText(getString(R.string.strPorcentaje, porcen));

        //Sonido logo
        mp = MediaPlayer.create(this,R.raw.spell_tinkle);
        mp.start();

        animatorAlpha = ObjectAnimator.ofFloat(txtResultado, View.ALPHA,0.0f, 1.0f);
        animatorAlpha.setDuration(animationDuration);
        AnimatorSet animatorSetAlpha = new AnimatorSet();
        animatorSetAlpha.playTogether(animatorAlpha);
        animatorSetAlpha.start();

        animatorAlphaPor = ObjectAnimator.ofFloat(txtPorcentaje, View.ALPHA,0.0f, 1.0f);
        animatorAlphaPor.setDuration(animationDuration);
        AnimatorSet animatorSetAlphaPor = new AnimatorSet();
        animatorSetAlphaPor.playTogether(animatorAlphaPor);
        animatorSetAlphaPor.start();


    }

    private void Limpiar(){
        txtLista.setText("");
        txtResultado.setText("");
        txtPorcentaje.setText("");
        txtLista.requestFocus();
    }


}