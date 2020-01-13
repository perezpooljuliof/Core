package mx.com.core.db.base;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public interface SQLAutoEjecutable {
    /**
     * Funcion principal para ejecutar un Stored Procedure dinamico a la Base de datos
     * el cual devuelve uno o mas resultados
     *
     * @param storedProcedure
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public <T> List<T> getList(TipoLlamada tipoLlamada, Object storedProcedure, Class<T> clazz) throws InstantiationException, IllegalAccessException;

    /**
     * Funcion principal para ejecutar un Stored Procedure dinamico a la Base de datos
     * el cual devuelve hasta un resultado
     *
     * @param storedProcedure
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public <T> T getResult(TipoLlamada tipoLlamada, Object storedProcedure, Class<T> clazz) throws IllegalAccessException, InstantiationException;

    /**
     * Funcion para establecer el JdbcTemplate
     *
     * @param template
     */
    public void setJdbcTemplate(JdbcTemplate template);
}
