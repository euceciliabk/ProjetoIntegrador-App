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

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RoteiroViewModel extends ViewModel {
    MutableLiveData<List<PontoTuristico>> roteiro;

    String latitude;
    String longitude;
    String tempo;

    public LiveData<List<PontoTuristico>> getRoteiro() {
        if (roteiro == null) {
            roteiro = new MutableLiveData<List<PontoTuristico>>();
        }

        return roteiro;
    }

    public void loadRoteiro(String latitude, String longitude, String tempo) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                List<PontoTuristico> roteirolist = new ArrayList<>();
                HttpRequest httpRequest = new HttpRequest( Config.SERVER_URL_BASE + "/views/roteiro/calcularoteiromobile.php", "GET", "UTF-8");
                httpRequest.addParam("latitude_u",latitude);
                httpRequest.addParam("longitude_u", longitude);
                httpRequest.addParam("tempo_disp", tempo);
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
                            roteirolist.add(ponto);
                        }
                        roteiro.postValue(roteirolist);
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
