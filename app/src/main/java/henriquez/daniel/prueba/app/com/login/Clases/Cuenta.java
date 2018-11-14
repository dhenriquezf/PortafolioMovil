package henriquez.daniel.prueba.app.com.login.Clases;

public class Cuenta {

    private int NroCuenta;
    private int totalReunido;
    private int montoPagar;
    private int totalPagado;
    private String curso;

    public Cuenta(int nroCuenta, int totalReunido, int montoPagar, int totalPagado, String curso) {
        NroCuenta = nroCuenta;
        this.totalReunido = totalReunido;
        this.montoPagar = montoPagar;
        this.totalPagado = totalPagado;
        this.curso = curso;
    }

    public int getNroCuenta() {
        return NroCuenta;
    }

    public void setNroCuenta(int nroCuenta) {
        NroCuenta = nroCuenta;
    }

    public int getTotalReunido() {
        return totalReunido;
    }

    public void setTotalReunido(int totalReunido) {
        this.totalReunido = totalReunido;
    }

    public int getMontoPagar() {
        return montoPagar;
    }

    public void setMontoPagar(int montoPagar) {
        this.montoPagar = montoPagar;
    }

    public int getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(int totalPagado) {
        this.totalPagado = totalPagado;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
}
