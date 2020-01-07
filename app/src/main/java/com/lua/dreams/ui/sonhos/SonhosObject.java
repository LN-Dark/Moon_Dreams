package com.lua.dreams.ui.sonhos;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SonhosObject{
    public String id, titulo, sonho, data;

    private SimpleDateFormat dateTime;

    public String getData() {
        return data;
    }

    public String getId() {
        return id;
    }

    public String getSonho() {
        return sonho;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSonho(String sonho) {
        this.sonho = sonho;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }



}
