package mx.com.core.db.base;

import mx.com.core.db.param.ParamAccess;
import mx.com.core.db.param.Parameter;

public class BaseResultado extends BaseStore {
    private String mostrarResultado;
    private String numResultado;
    private String resultado;

    public String getMostrarResultado() {
        return mostrarResultado;
    }
    public void setMostrarResultado(String mostrarResultado) {
        this.mostrarResultado = mostrarResultado;
    }

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
