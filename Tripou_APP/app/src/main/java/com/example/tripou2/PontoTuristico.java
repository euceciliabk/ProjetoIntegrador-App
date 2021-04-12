package com.example.tripou2;

import android.graphics.Bitmap;

public class PontoTuristico {

    public int Pid;
    public Bitmap Foto;
    public String Nome;
    public String Tempo;
    public String Descricao;
    public int Avaliacao;
    public String Localizacao;
    public int Preco;
    public String Latitude;
    public String Longitude;

    public PontoTuristico(int pid, Bitmap foto, String nome,  String tempo, String descricao, int avaliacao, String localizacao, int preco, String latitude, String longitude) {
        Pid = pid;
        Foto = foto;
        Nome = nome;
        Tempo = tempo;
        Descricao = descricao;
        Avaliacao = avaliacao;
        Localizacao = localizacao;
        Preco = preco;
        Latitude = latitude;
        Longitude = longitude;
    }

    public PontoTuristico(int pid, Bitmap foto, String nome, String descricao) {
        Pid = pid;
        Foto = foto;
        Nome = nome;
        Descricao = descricao;
    }

    public PontoTuristico(int pid) {
        Pid = pid;
    }

    public int getPid() {
        return Pid;
    }

    public Bitmap getFoto() {
        return Foto;
    }

    public String getNome() {
        return Nome;
    }

    public String getTempo() {
        return Tempo;
    }

    public String getDescricao() {
        return Descricao;
    }

    public int getAvaliacao() {
        return Avaliacao;
    }

    public String getLocalizacao() {
        return Localizacao;
    }

    public int getPreco() {
        return Preco;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }
}
