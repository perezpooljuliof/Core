package mx.com.core.db;

import mx.com.core.db.base.SQLAutoEjecutable;
import mx.com.core.db.param.ParamAccess;
import mx.com.core.db.param.ParamType;
import mx.com.core.db.param.Parameter;
import mx.com.core.db.base.TipoLlamada;
import mx.com.core.utilidades.MapSqlParameterSourceImpl;
import mx.com.core.utilidades.ReflectionManager;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MySQLCallGenerator implements SQLAutoEjecutable {
    private JdbcTemplate jdbcTemplate;// = SpringInitializer.getApplicationContext().getBean(JdbcTemplate.class)

    /*************************************************************************************/
    /**
     * Funcion que devuelve el nombre del StoredProcedure a partir de un Bean y el Tipo de Llamado que se utilizara.
     * Ejemplo para con la clase Usuario y tipo Alta devolveria el nombre USUARIOALT.
     *
     * @param tipoLlamada Tipo de Llamada
     * @param spBean Bean correspondiente a la tabla
     * @return
     */
    private String getJdbcProcedureName(TipoLlamada tipoLlamada, Object spBean) {
        Class clazz = spBean.getClass();
        StringBuilder spName = new StringBuilder(clazz.getSimpleName().toUpperCase());
        spName.append(tipoLlamada.getSubFijo());
        return spName.toString();
    }

    /**
     * Devuelve el Llamado SQL dependiendo del tipo de Llamado que se utilizara.
     *
     * @param tipoLlamada
     * @param storedProcedure
     * @return
     * @throws IllegalAccessException
     */
    private SimpleJdbcCall getJdbcCall(TipoLlamada tipoLlamada, Object storedProcedure) throws IllegalAccessException {
        SimpleJdbcCall simpleJdbcCall = null;
        //Obtenemos el nombre del SP
        String spName = getJdbcProcedureName(tipoLlamada, storedProcedure);
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate.getDataSource()).withProcedureName(spName)
                .returningResultSet("resultset",new ColumnMapRowMapper());

        return simpleJdbcCall;
    }

    /**
     * Funcion que devuelve un Bean con el nombre y valor de un Parametro
     *
     * @param storeProcedure
     * @param field
     * @return
     * @throws IllegalAccessException
     */
    private ParameterBean getJdbcParameter(Object storeProcedure, Field field) throws IllegalAccessException {
        ParameterBean parameterBean;
        Parameter parameterAnnotation = null;
        String reflectionParameterName = null;
        String parameterName = null;
        ParamType paramType = null;

        field.setAccessible(true);

        if(ReflectionManager.isAnnotationPresent(field, Parameter.class)) {
            parameterAnnotation = (Parameter) ReflectionManager.getAnnotation(field, Parameter.class);
            reflectionParameterName = parameterAnnotation.name();
            paramType = parameterAnnotation.type();
        }

        //Obtenemos el nombre del parametro
        if(reflectionParameterName == null || "".equals(reflectionParameterName)) {
            String name = field.getName();
            StringBuilder builder = new StringBuilder("Par_").append(name.substring(0, 1).toUpperCase()).append(name.substring(1));
            parameterName = builder.toString();
        }
        else {
            parameterName = reflectionParameterName;
        }

        //Obtenemos el valor del parametro
        Object value = field.get(storeProcedure);

        //Obtenemos el tipo de acceso el parametro
        ParamAccess access = parameterAnnotation.access();

        parameterBean = new ParameterBean();
        parameterBean.setName(parameterName);
        parameterBean.setValue(value);
        parameterBean.setTypeName(paramType!=null?paramType.getName():"");
        parameterBean.setType(paramType!=null?paramType.getType():0);
        parameterBean.setAccess(access);
        return parameterBean;
    }

    /**
     * Funcion que devuelve la lista de parametros y sus valores.
     * @param storeProcedure
     * @return
     * @throws IllegalAccessException
     */
    private MapSqlParameterSourceImpl getJdbcParameterSource(TipoLlamada tipoLlamada, Object storeProcedure) throws IllegalAccessException {
        MapSqlParameterSourceImpl sqlParameterSource = new MapSqlParameterSourceImpl();
        List<Field> fields = ReflectionManager.getFields(storeProcedure);

        for(Field field: fields) {
            boolean tipoLlamadaValido = false;
            //Ignoramos el campo o variable si no encontramos la anotacion de parametro.
            if(!ReflectionManager.isAnnotationPresent(field, Parameter.class)) {
                continue;
            }

            Parameter parametro = (Parameter) ReflectionManager.getAnnotation(field, Parameter.class);
            TipoLlamada[] tiposLlamada = parametro.targets();
            for(TipoLlamada tipoLlamadaVariable: tiposLlamada) {
                if(tipoLlamada == tipoLlamadaVariable) {
                    tipoLlamadaValido = true;
                }
            }

            //Ignoramos el campo o variable en caso de que no aplique para el tipo de llamada o SP
            if(!tipoLlamadaValido) {
                continue;
            }

            ParameterBean parameterBean = getJdbcParameter(storeProcedure, field);
            sqlParameterSource.addValue(parameterBean);
            //sqlParameterSource.addValue(parameterBean.getName(), parameterBean.getValue());
        }

        return sqlParameterSource;
    }

    /*************************************************************************************/
    /**
     * Funcion para imprimir el llamada a la base de datos.
     * @param simpleJdbcCall
     * @param in
     * @return
     */
    private String jdbcCallToString(SimpleJdbcCall simpleJdbcCall, MapSqlParameterSourceImpl in) {
        String nombreSP = simpleJdbcCall.getProcedureName();
        StringBuilder sqlStringBuilder = new StringBuilder();

        sqlStringBuilder.append("CALL ").append(nombreSP).append("(");


        for(String parameterName: in.getParameterNames()) {
            Object value = in.getValue(parameterName);
            ParameterBean parameterConfig = (ParameterBean) in.getParameter(parameterName);

            if(parameterConfig.getAccess()==ParamAccess.OUT || parameterConfig.getAccess()==ParamAccess.INOUT) {
                sqlStringBuilder.append("@").append(parameterName).append(",");
                continue;
            }

            if(value==null) {
                sqlStringBuilder.append("NULL").append(",");
            }
            else if("".equals(value)) {
                sqlStringBuilder.append("''").append(",");
            }
            else {
                sqlStringBuilder.append("'").append(value).append("'").append(",");
            }
        }
        sqlStringBuilder.deleteCharAt(sqlStringBuilder.toString().length() - 1).append(")");

        return sqlStringBuilder.toString();
    }

    private List<Map<String, Object>> execute(TipoLlamada tipoLlamada, Object storedProcedure) throws InstantiationException, IllegalAccessException {
        SimpleJdbcCall simpleJdbcCall = getJdbcCall(tipoLlamada, storedProcedure);
        MapSqlParameterSourceImpl in = getJdbcParameterSource(tipoLlamada, storedProcedure);
        System.out.println(jdbcCallToString(simpleJdbcCall, in));
        Map<String, Object> out = simpleJdbcCall.execute(in);
        simpleJdbcCall.withReturnValue();

        List<Map<String, Object>> resultSet = (List<Map<String, Object>>) out.get("resultset");

        return resultSet;
    }

    /*************************************************************************************/
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

    /***************************************************************************************/
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
    public <T> List<T> getList(TipoLlamada tipoLlamada, Object storedProcedure,Class<T> clazz) throws InstantiationException, IllegalAccessException {
        List<T> results = new ArrayList<>();

        List<Map<String, Object>> resultSet = execute(tipoLlamada, storedProcedure);
        for(Map<String, Object> objeto: resultSet) {
            T response = getJdbcResult(objeto, clazz);
            results.add(response);
        }

        return results;
    }

    /**
     * Funcion principal para ejecutar un Stored Procedure dinamico a la Base de datos
     * el cual devuelve un solo resultado
     *
     * @param storedProcedure
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Override
    public <T> T getResult(TipoLlamada tipoLlamada, Object storedProcedure, Class<T> clazz)
            throws IllegalAccessException, InstantiationException {
        T result = null;

        List<Map<String, Object>> resultSet = execute(tipoLlamada, storedProcedure);
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
