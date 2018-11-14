package henriquez.daniel.prueba.app.com.login.Clases;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {

    public int rut;
    public String digitoV;
    public String correo;
    public String password;
    public String nombre;
    public String aPaterno;
    public String aMaterno;
    //public ArrayList<Perfil> listaPerfiles;

    public Usuario(int rut, String digitoV, String correo, String password, String nombre, String aPaterno, String aMaterno/*, ArrayList<Perfil> listaPerfiles*/) {
        this.rut = rut;
        this.digitoV = digitoV;
        this.correo = correo;
        this.password = password;
        this.nombre = nombre;
        this.aPaterno = aPaterno;
        this.aMaterno = aMaterno;
        //this.listaPerfiles = listaPerfiles;
    }

    public int getRut() {
        return rut;
    }

    public void setRut(int rut) {
        this.rut = rut;
    }

    public String getDigitoV() {
        return digitoV;
    }

    public void setDigitoV(String digitoV) {
        this.digitoV = digitoV;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getaPaterno() {
        return aPaterno;
    }

    public void setaPaterno(String aPaterno) {
        this.aPaterno = aPaterno;
    }

    public String getaMaterno() {
        return aMaterno;
    }

    public void setaMaterno(String aMaterno) {
        this.aMaterno = aMaterno;
    }

    /*
    public ArrayList<Perfil> getListaPerfiles() {
        return listaPerfiles;
    }

    public void setListaPerfiles(ArrayList<Perfil> listaPerfiles) {
        this.listaPerfiles = listaPerfiles;
    }

    */
}
