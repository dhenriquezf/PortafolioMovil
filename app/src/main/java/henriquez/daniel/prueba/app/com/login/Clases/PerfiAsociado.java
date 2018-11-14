package henriquez.daniel.prueba.app.com.login.Clases;

public class PerfiAsociado {

    public int asociado_Id;
    public int perfil;
    public int rut;

    public PerfiAsociado(int asociado_Id, int perfil, int rut) {
        this.asociado_Id = asociado_Id;
        this.perfil = perfil;
        this.rut = rut;
    }

    public int getAsociado_Id() {
        return asociado_Id;
    }

    public void setAsociado_Id(int asociado_Id) {
        this.asociado_Id = asociado_Id;
    }

    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }

    public int getRut() {
        return rut;
    }

    public void setRut(int rut) {
        this.rut = rut;
    }
}
