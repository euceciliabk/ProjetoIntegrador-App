package com.example.tripou2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CadastroPontoActivity extends AppCompatActivity {

    String currentphotopath = "";
    static int PHOTO_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_ponto);

        ImageView imgFoto = findViewById(R.id.imgFoto);
        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PHOTO_PICKER_REQUEST);
            }
        });

        Button btnRegister =  findViewById(R.id.btnCadastro);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(currentphotopath.isEmpty()) {
                    Toast.makeText(CadastroPontoActivity.this, "Foto não selecionada", Toast.LENGTH_LONG).show();
                    return;
                }

                String login = Config.getLogin(CadastroPontoActivity.this);
                String senha = Config.getPassword(CadastroPontoActivity.this);

                EditText etNome =  findViewById(R.id.etNome);
                final String nome = etNome.getText().toString();
                if(nome.isEmpty()) {
                    Toast.makeText(CadastroPontoActivity.this, "Campo de nome não preenchido", Toast.LENGTH_LONG).show();
                    return;
                }

                Spinner spTempo =  findViewById(R.id.spTempo);
                final String tempo = spTempo.getSelectedItem().toString();
                if(tempo.isEmpty()) {
                    Toast.makeText(CadastroPontoActivity.this, "Campo de tempo não preenchido", Toast.LENGTH_LONG).show();
                    return;
                }

                Spinner spAvaliacao =  findViewById(R.id.spAvaliacao);
                final String avaliacao = spAvaliacao.getSelectedItem().toString();
                if(avaliacao.isEmpty()) {
                    Toast.makeText(CadastroPontoActivity.this, "Campo de avaliação não preenchido", Toast.LENGTH_LONG).show();
                    return;
                }

                EditText etPreco =  findViewById(R.id.etPreco);
                final String preco = etPreco.getText().toString();
                if(preco.isEmpty()) {
                    Toast.makeText(CadastroPontoActivity.this, "Campo de preço não preenchido", Toast.LENGTH_LONG).show();
                    return;
                }

                EditText etDescricao =  findViewById(R.id.tvDescricao);
                final String descricao = etDescricao.getText().toString();
                if(descricao.isEmpty()) {
                    Toast.makeText(CadastroPontoActivity.this, "Campo de descrição não preenchido", Toast.LENGTH_LONG).show();
                    return;
                }

                EditText etLocalizacao =  findViewById(R.id.etLocalizacao);
                final String localizacao = etLocalizacao.getText().toString();
                if(localizacao.isEmpty()) {
                    Toast.makeText(CadastroPontoActivity.this, "Campo de localização não preenchido", Toast.LENGTH_LONG).show();
                    return;
                }

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        HttpRequest httpRequest = new HttpRequest(Config.SERVER_URL_BASE + "views/ponto/inserepontomobile.php", "POST", "UTF-8");

                        Geocoder geocoder = new Geocoder(CadastroPontoActivity.this);
                        List<Address> addresses = new ArrayList<>();
                        try {
                            addresses = geocoder.getFromLocationName(localizacao, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(addresses.size() > 0) {
                            double latitude= addresses.get(0).getLatitude();
                            double longitude= addresses.get(0).getLongitude();
                            httpRequest.addParam("longitude", Double.toString(longitude));
                            httpRequest.addParam("latitude", Double.toString(latitude));
                        }
                        else {
                            httpRequest.addParam("longitude", "-40.217155324707086");
                            httpRequest.addParam("latitude", "-20.197340366202024");
                        }

                        try {

                            httpRequest.addParam("login", login);
                            httpRequest.addParam("senha", senha);
                            httpRequest.addParam("nome", nome);
                            httpRequest.addParam("tempo", tempo);
                            httpRequest.addParam("avaliacao", avaliacao);
                            httpRequest.addParam("quanto", preco);
                            httpRequest.addParam("texto", descricao);
                            httpRequest.addParam("local", localizacao);
                            httpRequest.addFile("foto",new File(currentphotopath));

                            InputStream is = httpRequest.execute();
                            String result = Util.inputStream2String(is, "UTF-8");
                            httpRequest.finish();

                            JSONObject jsonObject = new JSONObject(result);
                            final int success = jsonObject.getInt("sucesso");
                            if(success == 1) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(CadastroPontoActivity.this, "Novo ponto turístico registrado com sucesso", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(CadastroPontoActivity.this, MenuPrincipalActivity.class);
                                        startActivity(i);
                                    }
                                });
                            }
                            else {
                                final String error = jsonObject.getString("erro");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(CadastroPontoActivity.this, error, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    private File createImageFile() throws IOException {  // função para criar a imagem
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());  // transformar a data em uma string
        String imageFileName = "JPEG" + timeStamp;  // nome do arquivo
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File f = File.createTempFile(imageFileName, ".jpg", storageDir);
        return f;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PHOTO_PICKER_REQUEST){
            if(resultCode == Activity.RESULT_OK){
                Uri selectPhotoLocation = data.getData();
                Bitmap bmp = null;
                try {
                    bmp = Util.getBitmap(this, selectPhotoLocation, 4);
                    File f = createImageFile();
                    Util.saveBitmap(f.getAbsolutePath(), bmp);
                    ImageView imgBtn = findViewById(R.id.imgFoto);
                    imgBtn.setImageBitmap(bmp);
                    currentphotopath = f.getAbsolutePath();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}