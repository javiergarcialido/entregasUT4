package com.jgarcia.objetos;

import org.bson.types.ObjectId;

public class Usuario {
    private ObjectId id;
    private String usuario;
    private String contraseña;
    private boolean socio;

    public Usuario() {
    }

    public Usuario(ObjectId id, String usuario, String contraseña, boolean socio) {
        this.id = id;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.socio = socio;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public boolean isSocio() {
        return socio;
    }

    public void setSocio(boolean socio) {
        this.socio = socio;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", usuario='" + usuario + '\'' +
                ", contraseña='" + contraseña + '\'' +
                ", socio=" + socio +
                '}';
    }
}