package mx.com.core.db;

import mx.com.core.utilidades.ReflectionManager;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MySQLAutoEjecutable implements SQLAutoEjecutable {
    private JdbcTemplate jdbcTemplate; // = SpringInitializer.getApplicationContext().getBean(JdbcTemplate.class)

    /**
     * Funcion que devuelve el nombre del StoredProcedure a partir de un Bean.
     *
     * @param storedProcedure
     * @return
     */
    private String getJdbcProcedureName(Object storedProcedure) {
        Class clazz = storedProcedure.getClass();
        String spName = clazz.getSimpleName().toUpperCase();
        if (storedProcedure.getClass().isAnnotationPresent(StoredProcedure.class)) {
            StoredProcedure spAnnotation = storedProcedure.getClass().getAnnotation(StoredProcedure.class);
            if(!"".equals(spAnnotation.name())) {
                spName = spAnnotation.name();
            }
        }

        return spName;
    }

    private SimpleJdbcCall getJdbcCall(Object storedProcedure) throws IllegalAccessException {
        SimpleJdbcCall simpleJdbcCall = null;

        String spName = getJdbcProcedureName(storedProcedure);
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate.getDataSource()).withProcedureName(spName)
                .returningResultSet("resultset",new ColumnMapRowMapper());

        return simpleJdbcCall;
    }

    private SqlParameterSource getJdbcParameterSource(Object storeProcedure) throws IllegalAccessException {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        List<Field> fields = ReflectionManager.getFields(storeProcedure);
        for(Field field: fields) {
            ParameterBean parameterBean = getJdbcParameter(storeProcedure, field);
            sqlParameterSource.addValue(parameterBean.getName(), parameterBean.getValue());
        }

        return sqlParameterSource;
    }

    private ParameterBean getJdbcParameter(Object storeProcedure, Field field) throws IllegalAccessException {
        ParameterBean parameterBean;

        field.setAccessible(true);
        Object value = field.get(storeProcedure);
        String name = field.getName();
        if (field.isAnnotationPresent(Parameter.class)) {
            Parameter pAnnotation = field.getAnnotation(Parameter.class);
            String pName = pAnnotation.name();
            if(pName != null && !pName.equals("")) {
                name = pName;
            }
        }


        parameterBean = new ParameterBean();
        parameterBean.setName(name);
        parameterBean.setValue(value);
        return parameterBean;
    }

    private <T> T getJdbcResult(Map<String, Object> row, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T instancia = clazz.newInstance();

        List<Field> fields = ReflectionManager.getFields(clazz);
        for (Field field : fields) {
            try {
                setJdbcResultFieldValue(row, instancia, field);
            }
            catch(Exception e) { }
        }

        return instancia;
    }

    /**
     * Obtiene el valor de un campo del ResultSet
     * @param row
     * @param instancia
     * @param field
     * @throws IllegalAccessException
     */
    private void setJdbcResultFieldValue(Map<String, Object> row, Object instancia, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        Object fieldValue = null;

        if (field.isAnnotationPresent(Parameter.class)) {
            Parameter pAnnotation = field.getAnnotation(Parameter.class);
            ParamType type = pAnnotation.type();
            String name = pAnnotation.name();
            fieldValue = row.get(name);
        }

        String fieldName = field.getName();
        fieldValue = row.get(fieldName);
        field.set(instancia, fieldValue.toString());
    }

    private String jdbcCallToString(SimpleJdbcCall simpleJdbcCall, SqlParameterSource in) {
        String nombreSP = simpleJdbcCall.getProcedureName();
        StringBuilder sqlStringBuilder = new StringBuilder();

        sqlStringBuilder.append("CALL ").append(nombreSP).append("(");
        for(String parameterName: in.getParameterNames()) {
            Object value = in.getValue(parameterName);
            if("".equals(value)) {
                value = "''";
            }
            sqlStringBuilder.append(value).append(",");
        }
        sqlStringBuilder.deleteCharAt(sqlStringBuilder.toString().length() - 1).append(")");

        return sqlStringBuilder.toString();
    }

    private List<Map<String, Object>> execute(Object storedProcedure) throws InstantiationException, IllegalAccessException {
        SimpleJdbcCall simpleJdbcCall = getJdbcCall(storedProcedure);
        SqlParameterSource in = getJdbcParameterSource(storedProcedure);
        System.out.println(jdbcCallToString(simpleJdbcCall, in));
        Map<String, Object> out = simpleJdbcCall.execute(in);
        simpleJdbcCall.withReturnValue();

        List<Map<String, Object>> resultSet = (List<Map<String, Object>>) out.get("resultset");

        return resultSet;
    }

    /**
     * Funcion principal para ejecutar un Stored Procedure dinamico a la Base de datos
     * el cual devuelve uno o mas resultados
     *
     * @param storedProcedure
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Override
    public <T> List<T> getList(Object storedProcedure,Class<T> clazz) throws InstantiationException, IllegalAccessException {
        List<T> results = new ArrayList<>();

        List<Map<String, Object>> resultSet = execute(storedProcedure);
        for(Map<String, Object> objeto: resultSet) {
            T response = getJdbcResult(objeto, clazz);
            results.add(response);
        }

        return results;
    }

    /**
     * Funcion principal para ejecutar un Stored Procedure dinamico a la Base de datos
     * el cual devuelve hasta un resultado
     *
     * @param storedProcedure
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Override
    public <T> T getResult(Object storedProcedure, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T result = null;

        List<Map<String, Object>> resultSet = execute(storedProcedure);
        if(resultSet.size() > 0) {
            Map<String, Object> resultSetMap = resultSet.get(0);
            result = getJdbcResult(resultSetMap, clazz);
        }

        return result;
    }

    @Override
    public void setJdbcTemplate(JdbcTemplate template) {
        this.jdbcTemplate = template;
    }
}
