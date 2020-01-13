package mx.com.core.db;

import mx.com.core.db.base.TipoLlamada;
import mx.com.core.utilidades.LongUtils;

public class MySQLAutoEjecutable {
    private MySQLCallGenerator mySQLCallGenerator;
    private LongUtils longUtils;

    public <T> T alta(Object storedProcedure, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        return mySQLCallGenerator.getResult(TipoLlamada.ALTA, storedProcedure, clazz);
    }

    public Long generateUniqueID() {
        return longUtils.generateUniqueID();
    }

    public MySQLCallGenerator getMySQLCallGenerator() {
        return mySQLCallGenerator;
    }
    public void setMySQLCallGenerator(MySQLCallGenerator mySQLCallGenerator) {
        this.mySQLCallGenerator = mySQLCallGenerator;
    }

    public LongUtils getLongUtils() {
        return longUtils;
    }
    public void setLongUtils(LongUtils longUtils) {
        this.longUtils = longUtils;
    }
}
