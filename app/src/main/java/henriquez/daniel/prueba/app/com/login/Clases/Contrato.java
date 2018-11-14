package henriquez.daniel.prueba.app.com.login.Clases;

public class Contrato {
    private int id;
    private String descripcion;
    private String nombre;
    private String fecha;
    private int totalViaje;
    private int curso;
    private int cantServicios;
    private int cantSeguros;
    private int cantDestinos;

    public Contrato(int id, String descripcion, String nombre, String fecha, int totalViaje, int curso, int cantServicios, int cantSeguros, int cantDestinos) {
        this.id = id;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.fecha = fecha;
        this.totalViaje = totalViaje;
        this.curso = curso;
        this.cantServicios = cantServicios;
        this.cantSeguros = cantSeguros;
        this.cantDestinos = cantDestinos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getTotalViaje() {
        return totalViaje;
    }

    public void setTotalViaje(int totalViaje) {
        this.totalViaje = totalViaje;
    }

    public int getCurso() {
        return curso;
    }

    public void setCurso(int curso) {
        this.curso = curso;
    }

    public int getCantServicios() {
        return cantServicios;
    }

    public void setCantServicios(int cantServicios) {
        this.cantServicios = cantServicios;
    }

    public int getCantSeguros() {
        return cantSeguros;
    }

    public void setCantSeguros(int cantSeguros) {
        this.cantSeguros = cantSeguros;
    }

    public int getCantDestinos() {
        return cantDestinos;
    }

    public void setCantDestinos(int cantDestinos) {
        this.cantDestinos = cantDestinos;
    }
}
