package mx.com.core.db.base;

import mx.com.core.db.param.ParamType;
import mx.com.core.db.param.Parameter;

public class BaseStore {
    @Parameter(type = ParamType.BIGINT)
    protected long uuid;
    @Parameter
    protected int idUsuario;
    @Parameter
    protected String programa;

    public long getUuid() {
        return uuid;
    }
    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPrograma() {
        return programa;
    }
    public void setPrograma(String programa) {
        this.programa = programa;
    }
}
