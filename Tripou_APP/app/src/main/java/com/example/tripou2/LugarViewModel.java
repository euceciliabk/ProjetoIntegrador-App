package com.example.tripou2;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tripou2.Activity.LugarActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class LugarViewModel extends ViewModel {

    int pid;
    MutableLiveData<PontoTuristico> ponto;

    public LugarViewModel(int pid) {
        this.pid = pid;
    }

    public LiveData<PontoTuristico> getPonto() {
        if (this.ponto == null) {
            ponto = new MutableLiveData<PontoTuristico>();
            loadPonto();
        }
        return ponto;
    }

    void loadPonto() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                List<PontoTuristico> pontoturisticoslist = new ArrayList<>();
                HttpRequest httpRequest = new HttpRequest( Config.SERVER_URL_BASE + "/views/ponto/getpontomobile.php", "GET", "UTF-8");
                httpRequest.addParam("id",String.valueOf(pid));
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
                        String avaliacao = jsonObject.getString("avaliacao");
                        String localizacao = jsonObject.getString("local");
                        String tempo = jsonObject.getString("tempo");
                        String preco = jsonObject.getString("quanto");
                        String latitude = jsonObject.getString("latitude");
                        String longitude = jsonObject.getString("longitude");

                        PontoTuristico p = new PontoTuristico(Integer.valueOf(pid), bmp, nome, tempo, descricao, Integer.valueOf(avaliacao), localizacao, Integer.valueOf(preco), latitude, longitude);
                        ponto.postValue(p);
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    static public class LugarViewModelFactory implements ViewModelProvider.Factory {

        int pid;

        public LugarViewModelFactory(int pid) {
            this.pid = pid;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new LugarViewModel(pid);
        }
    }
}
