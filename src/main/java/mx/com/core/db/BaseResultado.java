package mx.com.core.db;

public class BaseResultado {
    @Parameter(name="NumResultado", type = ParamType.INT)
    private String numResultado;
    @Parameter(name="Resultado", type = ParamType.VARCHAR)
    private String resultado;


    public String getNumResultado() {
        return numResultado;
    }

    public void setNumResultado(String numResultado) {
        this.numResultado = numResultado;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
