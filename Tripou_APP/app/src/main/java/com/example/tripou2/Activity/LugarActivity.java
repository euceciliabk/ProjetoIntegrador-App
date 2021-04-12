package com.example.tripou2.Activity;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tripou2.LugarViewModel;
import com.example.tripou2.PontoTuristico;
import com.example.tripou2.R;

public class LugarActivity extends AppCompatActivity {

    String latitude;
    String longitude;

    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_lugar);

       Intent i = getIntent();
       int pid = i.getIntExtra("id",0);

       LugarViewModel lugarViewModel = new ViewModelProvider(this, new LugarViewModel.LugarViewModelFactory(pid)).get(LugarViewModel.class);
       final LiveData<PontoTuristico> ponto = lugarViewModel.getPonto();
       ponto.observe(this, new Observer<PontoTuristico>() {
           @Override
           public void onChanged(PontoTuristico pontoTuristico) {
               latitude = pontoTuristico.getLatitude();
               longitude = pontoTuristico.getLongitude();
               TextView tvNome = findViewById(R.id.tvNome);
               tvNome.setText(pontoTuristico.getNome());
               TextView tvDescricao = findViewById(R.id.tvDescricao);
               tvDescricao.setText(pontoTuristico.getDescricao());
               TextView tvTempo = findViewById(R.id.tvTempo);
               tvTempo.setText(pontoTuristico.getTempo());
               TextView tvAvaliacao = findViewById(R.id.tvAvaliacao);
               tvAvaliacao.setText(String.valueOf(pontoTuristico.getAvaliacao()));
               TextView tvPreco = findViewById(R.id.tvPreco);
               tvPreco.setText(String.valueOf(pontoTuristico.getPreco()));
               TextView tvLocalizacao = findViewById(R.id.tvLocalizacao);
               tvLocalizacao.setText(pontoTuristico.getLocalizacao());
               ImageView foto = findViewById(R.id.imgFoto);
               foto.setImageBitmap(pontoTuristico.getFoto());
           }
       });

        ImageButton btnMaps = findViewById(R.id.btnMaps);
        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriLocation;
                uriLocation = Uri.parse("geo:" + latitude + "," + longitude);
                Intent mapsIntent = new Intent(Intent.ACTION_VIEW, uriLocation);
                mapsIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapsIntent);
            }
        });
    }
}