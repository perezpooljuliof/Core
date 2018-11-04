package mx.com.core.db;

public class TransaccionBaseStore extends BaseStore {
    private String par_MostrarResultado;
    @Parameter(type = ParamType.INT, access = ParamAccess.INOUT)
    private int par_NumResultado;
    @Parameter(type = ParamType.VARCHAR, access = ParamAccess.INOUT)
    private String par_Resultado;

    public String getPar_MostrarResultado() {
        return par_MostrarResultado;
    }

    public void setPar_MostrarResultado(String par_MostrarResultado) {
        this.par_MostrarResultado = par_MostrarResultado;
    }

    public int getPar_NumResultado() {
        return par_NumResultado;
    }

    public void setPar_NumResultado(int par_NumResultado) {
        this.par_NumResultado = par_NumResultado;
    }

    public String getPar_Resultado() {
        return par_Resultado;
    }

    public void setPar_Resultado(String par_Resultado) {
        this.par_Resultado = par_Resultado;
    }
}
