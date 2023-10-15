package com.example.piedra_papel_tijeras;

import static android.app.ProgressDialog.show;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {

    private TextInputEditText _nombreJugador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this._nombreJugador = findViewById(R.id.nombreJugador);
    }

    public void onClick(View view) {
        String nombreJugador = _nombreJugador.getText().toString().trim();

        if(!nombreJugador.isEmpty()){
            Intent intent= new Intent(this, MainActivity.class);
            intent.putExtra(  "Jugador", nombreJugador);
            startActivity(intent);
            finish();

        }else {
            Toast.makeText(this, "Por favor Escribe tu nombre", Toast.LENGTH_SHORT).show();
            //Abrir teclado del telefono
            _nombreJugador.requestFocus();
            InputMethodManager imm= (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE );
            imm.showSoftInput(_nombreJugador, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}