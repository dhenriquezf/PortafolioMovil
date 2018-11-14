package henriquez.daniel.prueba.app.com.login.Clases;

import java.util.ArrayList;

public class Curso {

    private int idCurso;
    private String Nombre;
    private int colegio;
    private int totalReunido;

    public Curso(int idCurso, String nombre, int colegio, int totalReunido) {
        this.idCurso = idCurso;
        Nombre = nombre;
        this.colegio = colegio;
        this.totalReunido = totalReunido;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int getColegio() {
        return colegio;
    }

    public void setColegio(int colegio) {
        this.colegio = colegio;
    }

    public int getTotalReunido() {
        return totalReunido;
    }

    public void setTotalReunido(int totalReunido) {
        this.totalReunido = totalReunido;
    }
}
