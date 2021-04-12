package com.example.tripou2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MenuPrincipalViewModel extends ViewModel {

    MutableLiveData<List<PontoTuristico>> pontosturisticos;

    public LiveData<List<PontoTuristico>> getPontosTuristicos() {
        if (pontosturisticos == null) {
            pontosturisticos = new MutableLiveData<List<PontoTuristico>>();
        }

        loadPontosTuristicos();

        return pontosturisticos;
    }

    public void refreshPontosTuristicos(){
        loadPontosTuristicos();
    }

    void loadPontosTuristicos() {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                List<PontoTuristico> pontoturisticoslist = new ArrayList<>();
                HttpRequest httpRequest = new HttpRequest( Config.SERVER_URL_BASE + "/views/ponto/listapontomobile.php", "POST", "UTF-8");
                try {
                    InputStream is = httpRequest.execute();
                    String result = Util.inputStream2String(is, "UTF-8");
                    httpRequest.finish();

                    JSONObject jsonObject = new JSONObject(result);
                    int sucesso = jsonObject.getInt("sucesso");
                    if (sucesso == 1){
                        JSONArray jsonArray = jsonObject.getJSONArray("idpontos");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            int idponto  = jsonArray.getInt(i);

                            PontoTuristico ponto = new PontoTuristico(idponto);
                            pontoturisticoslist.add(ponto);
                        }
                        pontosturisticos.postValue(pontoturisticoslist);
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}

