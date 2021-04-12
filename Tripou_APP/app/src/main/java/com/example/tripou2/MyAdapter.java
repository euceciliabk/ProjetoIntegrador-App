package com.example.tripou2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripou2.Activity.LugarActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyAdapter extends RecyclerView.Adapter {

    MenuPrincipalActivity menuPrincipalActivity;
    List<PontoTuristico> pontosturisticos;

    public MyAdapter(MenuPrincipalActivity menuPrincipalActivity, List <PontoTuristico> pontosturisticos) { //construtor
        this.menuPrincipalActivity = menuPrincipalActivity;
        this.pontosturisticos = pontosturisticos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //cria as interfaces, mas sem preenchê-las (cria os elementos da lista)
        LayoutInflater inflater = LayoutInflater.from(menuPrincipalActivity);
        View v = inflater.inflate(R.layout.ponto_item, parent, false); // v guarda o layout construído
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return new MyViewHolder(v); //guarda o item gerado dentro de MyViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        PontoTuristico ponto = this.pontosturisticos.get(position);

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

                        menuPrincipalActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tvnome = viewHolder.itemView.findViewById(R.id.tvNome);
                                tvnome.setText(nome);
                                TextView tvdescricao = viewHolder.itemView.findViewById(R.id.tvDescricao);
                                tvdescricao.setText(descricao);
                                ImageView imvfoto = viewHolder.itemView.findViewById(R.id.imvFoto);
                                imvfoto.setImageBitmap(bmp);
                            }
                        });
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(menuPrincipalActivity, LugarActivity.class);
                i.putExtra("id", ponto.getPid());
                menuPrincipalActivity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.pontosturisticos.size();
    }

}



