package com.example.tripou2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class RoteiroProntoActivity extends AppCompatActivity {
    static int NEW_ITEM_REQUEST = 1;

    MyAdapterRoteiro myAdapterRoteiro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roteiro_pronto);

        RoteiroViewModel viewModel = new ViewModelProvider(this).get(RoteiroViewModel.class);
        /*List<PontoTuristico> pontoTuristicos = viewModel.getItens();

        myAdapterRoteiro = new MyAdapterRoteiro(this, pontoTuristicos); //instância do MyAdapter

        //configurando RecyclerView:
        RecyclerView rvItens = findViewById(R.id.rvRoteiro);
        rvItens.setHasFixedSize(true); //diz que todos os itens da lista terão o mesmo tamanho

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);//diz de que forma os itens serão exibidos na lista, nesse cado, será de forma linear
        rvItens.setLayoutManager(layoutManager);

        rvItens.setAdapter(myAdapterRoteiro); //diz qual é o Adapter que vai construir os elementos da lista

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvItens.getContext(), DividerItemDecoration.VERTICAL);
        rvItens.addItemDecoration(dividerItemDecoration);*/

    }
}