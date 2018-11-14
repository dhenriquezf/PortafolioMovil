package henriquez.daniel.prueba.app.com.login.Clases;

import java.util.ArrayList;

public class Alumno {

    private int id;
    private String nombreCompleto;
    private int curso;
    private String apoderado;

    public Alumno(int id, String nombre, int curso, String apoderado) {
        this.id = id;
        this.nombreCompleto = nombre;
        this.curso = curso;
        this.apoderado = apoderado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public int getCurso() {
        return curso;
    }

    public void setCurso(int curso) {
        this.curso = curso;
    }

    public String getApoderado() {
        return apoderado;
    }

    public void setApoderado(String apoderado) {
        this.apoderado = apoderado;
    }

    @Override
    public String toString() {
        return nombreCompleto;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Alumno){
            Alumno c = (Alumno )obj;
            if(c.getNombreCompleto().equals(nombreCompleto) && c.getId()==id ) return true;
        }

        return false;
    }
}
