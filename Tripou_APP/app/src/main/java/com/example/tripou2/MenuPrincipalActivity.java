package com.example.tripou2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Observable;

public class MenuPrincipalActivity extends AppCompatActivity {
    static int NEW_ITEM_REQUEST = 1;

    MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        FloatingActionButton btnRoteiro = findViewById(R.id.btnRoteiro); //pega o botão da interface
        btnRoteiro.setOnClickListener(new View.OnClickListener() { //quando o botão é clicado, executa a função a seguir:
            @Override
            public void onClick(View v) {
                Intent i = new Intent (MenuPrincipalActivity.this, RoteiroActivity.class); //cria uma intenção de "viajar" da página de MainActivity para a de NewItemActivity
                startActivity(i); //executa a intenção e espera um resultado
            }
        });

        FloatingActionButton btnAdicionarPonto = findViewById(R.id.btnAdicionarPonto); //pega o botão da interface
        btnAdicionarPonto.setOnClickListener(new View.OnClickListener() { //quando o botão é clicado, executa a função a seguir:
            @Override
            public void onClick(View v) {
                Intent i = new Intent (MenuPrincipalActivity.this, CadastroPontoActivity.class); //cria uma intenção de "viajar" da página de MainActivity para a de NewItemActivity
                startActivity(i); //executa a intenção e espera um resultado
            }
        });

        RecyclerView rvMenu = findViewById(R.id.rvMenu);
        rvMenu.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvMenu.setLayoutManager(layoutManager);

        MenuPrincipalViewModel menuPrincipalViewModel = new ViewModelProvider(this).get(MenuPrincipalViewModel.class);

        LiveData<List<PontoTuristico>> pontosturisticos = menuPrincipalViewModel.getPontosTuristicos();
        pontosturisticos.observe(MenuPrincipalActivity.this, new Observer<List<PontoTuristico>>() {
            @Override
            public void onChanged(List<PontoTuristico> pontoTuristicos) {
                MyAdapter adapter = new MyAdapter(MenuPrincipalActivity.this, pontoTuristicos);
                rvMenu.setAdapter(adapter);
            }
        });
    }
}