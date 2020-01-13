package mx.com.core.db.base;

public enum TipoLlamada {
    ALTA("ALT"), MODIFICACION ("MOD"), CONSULTA("CON"), BAJA("BAJ"), PROCESO("PRO");

    TipoLlamada(String subFijo) {
        this.subFijo = subFijo;
    }

    String subFijo;
    public String getSubFijo() {
        return subFijo;
    }
}
