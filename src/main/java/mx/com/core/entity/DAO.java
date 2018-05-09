package mx.com.core.entity;

import mx.com.core.db.ParamAccess;
import mx.com.core.db.ParamType;
import mx.com.core.db.Parameter;
import mx.com.core.db.StoreProcedure;
import mx.com.core.utilidades.ReflectionManager;
import mx.com.core.utilidades.SpringInitializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DAO {
    private JdbcTemplate template = SpringInitializer.getApplicationContext().getBean(JdbcTemplate.class);

    public String getProcedureCall(Object storeProcedure) {
        StringBuilder call = new StringBuilder("CALL ");
        Class clazz = storeProcedure.getClass();
        if (storeProcedure.getClass().isAnnotationPresent(StoreProcedure.class)) {
            StoreProcedure spAnnotation = storeProcedure.getClass().getAnnotation(StoreProcedure.class);
            if("".equals(spAnnotation.name())) {
                call.append(clazz.getSimpleName()).append("(");
            }
            else {
                call.append(spAnnotation.name()).append("(");
            }
        }

        List<Field> fields = ReflectionManager.getFields(storeProcedure);
        for (Field field : fields) {
            try {
                String fieldValue = getParameterValue(storeProcedure, field);
                if(!StringUtils.isEmpty(fieldValue)) {
                    call.append(fieldValue).append(",");
                }
            }
            catch(Exception e) { }
        }

        call.deleteCharAt(call.toString().length() - 1).append(")");

        return call.toString();
    }

    private String getParameterValue(Object storeProcedure, Field field) throws IllegalAccessException {
        StringBuilder sb = null;

        field.setAccessible(true);
        if (field.isAnnotationPresent(Parameter.class)) {
            sb = new StringBuilder();
            Parameter pAnnotation = field.getAnnotation(Parameter.class);
            ParamAccess access = pAnnotation.access();
            ParamType type = pAnnotation.type();
            Object value = field.get(storeProcedure);

            switch (type) {
                case INT:
                case DOUBLE:
                    sb.append(value);
                    break;
                default:
                    sb.append("'").append(value).append("'");
                    break;
            }

            return sb.toString();
        }

        return null;
    }

    private <T> Object getResult(Map<String, Object> row,Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T instancia = clazz.newInstance();

        List<Field> fields = ReflectionManager.getFields(clazz);
        for (Field field : fields) {
            try {
                setFieldValue(row, instancia, field);
            }
            catch(Exception e) { }
        }

        return instancia;
    }

    private void setFieldValue(Map<String, Object> row, Object instancia, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        if (field.isAnnotationPresent(Parameter.class)) {
            Parameter pAnnotation = field.getAnnotation(Parameter.class);
            ParamAccess access = pAnnotation.access();
            ParamType type = pAnnotation.type();
            String name = pAnnotation.name();
            Object fieldValue = row.get(name);

            switch (type) {
                case INT:
                    field.set(instancia, fieldValue);
                    break;
                case DOUBLE:
                    field.set(instancia, fieldValue);
                    break;
                default:
                    field.set(instancia, fieldValue.toString());
                    break;
            }

        }
    }

    public List execute(Object storeProcedure) throws InstantiationException, IllegalAccessException {
        String query = getProcedureCall(storeProcedure);
        System.out.println(query);
        List<Map<String, Object>> rows = template.queryForList(query);
        List resultados = new ArrayList();
        for (Map row : rows) {
            Object result = getResult(row, storeProcedure.getClass());
            resultados.add(result);
        }

        return resultados;
    }


    public static void main(String[] args) {
        UsuarioLis usuarioLis = new UsuarioLis();
        usuarioLis.setAutoID(0);
        usuarioLis.setUserID(0);
        usuarioLis.setUserPass("");
        usuarioLis.setFirstName("");
        usuarioLis.setLastName("");

        try {
            DAO dao = new DAO();
            List<UsuarioLis> resultados = (List<UsuarioLis>) dao.execute(usuarioLis);
            for(UsuarioLis usuario: resultados) {
                System.out.println("usuario:" + usuario.getFirstName() + usuario.getUserID());
            }
        }
        catch (Exception e) {

        }

    }
}
