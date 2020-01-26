package mx.com.core.db;

import mx.com.core.db.base.TipoLlamada;
import mx.com.core.utilidades.LongUtils;

public class MySQLAutoEjecutable {
    private MySQLCallGenerator mySQLCallGenerator;

    public <T> T alta(Object bean, Class<T> resultClazz) throws InstantiationException, IllegalAccessException {
        return mySQLCallGenerator.getResult(TipoLlamada.ALTA, bean, resultClazz);
    }

    public MySQLCallGenerator getMySQLCallGenerator() {
        return mySQLCallGenerator;
    }
    public void setMySQLCallGenerator(MySQLCallGenerator mySQLCallGenerator) {
        this.mySQLCallGenerator = mySQLCallGenerator;
    }
}
