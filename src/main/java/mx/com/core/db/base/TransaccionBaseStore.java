package mx.com.core.db.base;

import mx.com.core.db.base.BaseStore;
import mx.com.core.db.base.TipoLlamada;
import mx.com.core.db.param.ParamAccess;
import mx.com.core.db.param.ParamType;
import mx.com.core.db.param.Parameter;

public class TransaccionBaseStore extends BaseStore {
    @Parameter
    protected String mostrarResultado;
    @Parameter(type = ParamType.INT, access = ParamAccess.INOUT)
    protected int numResultado;
    @Parameter(type = ParamType.VARCHAR, access = ParamAccess.INOUT)
    protected String resultado;


    public String getMostrarResultado() {
        return mostrarResultado;
    }
    public void setMostrarResultado(String mostrarResultado) {
        this.mostrarResultado = mostrarResultado;
    }

    public int getNumResultado() {
        return numResultado;
    }
    public void setNumResultado(int numResultado) {
        this.numResultado = numResultado;
    }

    public String getResultado() {
        return resultado;
    }
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
