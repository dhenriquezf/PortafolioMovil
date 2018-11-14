package henriquez.daniel.prueba.app.com.login.Clases;

public class Seguro {

    private int id;
    private String nombre;
    private String descripcion;
    private int cobertura;
    private int valor;
    private int tipo;

    public Seguro(int id, String nombre, String descripcion, int cobertura, int valor, int tipo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cobertura = cobertura;
        this.valor = valor;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCobertura() {
        return cobertura;
    }

    public void setCobertura(int cobertura) {
        this.cobertura = cobertura;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
