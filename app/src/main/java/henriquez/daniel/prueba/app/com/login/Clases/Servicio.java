package henriquez.daniel.prueba.app.com.login.Clases;

public class Servicio {
    private int id;
    private String descripcion;
    private String nombre;
    private int valor;

    public Servicio(int id, String descripcion, String nombre, int valor) {
        this.id = id;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.valor = valor;
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

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
