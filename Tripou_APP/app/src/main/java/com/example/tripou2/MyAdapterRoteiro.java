package com.example.tripou2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.tripou2.Activity.LugarActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapterRoteiro extends RecyclerView.Adapter {
    RoteiroActivity roteiroActivity;
    List<PontoTuristico> roteiro;

    public MyAdapterRoteiro(RoteiroActivity roteiroActivity, List<PontoTuristico> roteiro) { //construtor
        this.roteiroActivity = roteiroActivity;
        this.roteiro = roteiro;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //cria as interfaces, mas sem preenchê-las (cria os elementos da lista)
        LayoutInflater inflater = LayoutInflater.from(roteiroActivity);
        View v = inflater.inflate(R.layout.ponto_item, parent, false); // v guarda o layout construído
        return new MyViewHolder(v); //guarda o item gerado dentro de MyViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) { //preenche os elementos de interface com os dados
        PontoTuristico ponto = this.roteiro.get(position);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                List<PontoTuristico> pontoturisticoslist = new ArrayList<>();
                HttpRequest httpRequest = new HttpRequest( Config.SERVER_URL_BASE + "/views/ponto/getpontomobile.php", "GET", "UTF-8");
                httpRequest.addParam("id",String.valueOf(ponto.getPid()));
                try {
                    InputStream is = httpRequest.execute();
                    String result = Util.inputStream2String(is, "UTF-8");
                    httpRequest.finish();

                    JSONObject jsonObject = new JSONObject(result);
                    int sucesso = jsonObject.getInt("sucesso");
                    if (sucesso == 1){
                        String nome = jsonObject.getString("nome");
                        String descricao = jsonObject.getString("texto");
                        String foto = jsonObject.getString("conteudofoto");
                        Bitmap bmp = Util.base642Bitmap(foto);

                        roteiroActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tvnome = holder.itemView.findViewById(R.id.tvNome);
                                tvnome.setText(nome);
                                TextView tvdescricao = holder.itemView.findViewById(R.id.tvDescricao);
                                tvdescricao.setText(descricao);
                                ImageView imvfoto = holder.itemView.findViewById(R.id.imvFoto);
                                imvfoto.setImageBitmap(bmp);
                            }
                        });
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(roteiroActivity, LugarActivity.class);
                i.putExtra("id", ponto.getPid());
                roteiroActivity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() { //serve para informar ao RecyclerView quantos itens existem na lista
        return roteiro.size();
    }
}


