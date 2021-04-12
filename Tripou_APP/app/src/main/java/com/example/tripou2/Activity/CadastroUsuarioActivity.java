package com.example.tripou2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tripou2.Config;
import com.example.tripou2.HttpRequest;
import com.example.tripou2.Util;
import com.example.tripou2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CadastroUsuarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        Button btnRegister =  findViewById(R.id.btnCadastro);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etLogin =  findViewById(R.id.etLogin);
                final String login = etLogin.getText().toString();
                if(login.isEmpty()) {
                    Toast.makeText(CadastroUsuarioActivity.this, "Campo de login não preenchido", Toast.LENGTH_LONG).show();
                    return;
                }

                EditText etSenha =  findViewById(R.id.etSenha);
                final String senha = etSenha.getText().toString();
                if(senha.isEmpty()) {
                    Toast.makeText(CadastroUsuarioActivity.this, "Campo de senha não preenchido", Toast.LENGTH_LONG).show();
                    return;
                }

                EditText etNome =  findViewById(R.id.etNome);
                final String nome = etNome.getText().toString();
                if(nome.isEmpty()) {
                    Toast.makeText(CadastroUsuarioActivity.this, "Campo de nome não preenchido", Toast.LENGTH_LONG).show();
                    return;
                }

                /*EditText etNewPasswordCheck =  findViewById(R.id.etNewPasswordCheck);
                String newPasswordCheck = etNewPasswordCheck.getText().toString();
                if(newPasswordCheck.isEmpty()) {
                    Toast.makeText(CadastroUsuarioActivity.this, "Campo de checagem de senha não preenchido", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!newPassword.equals(newPasswordCheck)) {
                    Toast.makeText(CadastroUsuarioActivity.this, "Senha não confere", Toast.LENGTH_LONG).show();
                    return;
                }*/

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        HttpRequest httpRequest = new HttpRequest(Config.SERVER_URL_BASE + "views/usuario/insereloginsenhamobile.php", "POST", "UTF-8");
                        httpRequest.addParam("login", login);
                        httpRequest.addParam("senha", senha);
                        httpRequest.addParam("nome", nome);

                        try {
                            InputStream is = httpRequest.execute();
                            String result = Util.inputStream2String(is, "UTF-8");
                            httpRequest.finish();

                            JSONObject jsonObject = new JSONObject(result);
                            final int success = jsonObject.getInt("sucesso");
                            if(success == 1) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(CadastroUsuarioActivity.this, "Novo usuario registrado com sucesso", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                });
                            }
                            else {
                                final String error = jsonObject.getString("erro");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(CadastroUsuarioActivity.this, error, Toast.LENGTH_LONG).show();
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
}