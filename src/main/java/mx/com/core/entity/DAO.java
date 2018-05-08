package mx.com.core.entity;

import mx.com.core.db.ParamAccess;
import mx.com.core.db.ParamType;
import mx.com.core.db.Parameter;
import mx.com.core.db.StoreProcedure;
import mx.com.core.utilidades.ReflectionManager;

import java.lang.reflect.Field;
import java.util.List;

public class DAO {
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

        List<Field> campos = ReflectionManager.getFields(storeProcedure);
        for (Field field : campos) {
            try {
                call.append(getParameterValue(storeProcedure, field)).append(", ");
            }
            catch(Exception e) { }
        }

        call.delete(call.toString().length() - 3, call.toString().length() - 1).append(")");

        return call.toString();
    }

    private String getParameterValue(Object storeProcedure, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        StringBuilder sb = new StringBuilder();
        if (field.isAnnotationPresent(Parameter.class)) {
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
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        Usuario usuario = new Usuario();
        usuario.setNombre("mi nombre");
        usuario.setEdad(32);

        DAO dao = new DAO();
        String call = dao.getProcedureCall(usuario);
        System.out.println(call);
    }
}
