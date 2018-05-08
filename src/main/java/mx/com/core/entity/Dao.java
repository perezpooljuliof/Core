package mx.com.core.entity;

import mx.com.core.db.Parameter;
import mx.com.core.db.StoreProcedure;
import mx.com.core.utilidades.ReflectionManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public class Dao {
    public static void main(String[] args) {
        Usuario usuario = new Usuario();
        usuario.setNombre("mi nombre");
        usuario.setEdad(32);

        List<Field> campos = ReflectionManager.getFields(usuario);
        System.out.println(campos);

        for (Field field : campos) {
            if (field.isAnnotationPresent(Parameter.class)) {
                Parameter fAnno = field.getAnnotation(Parameter.class);
                System.out.println("Field: " + field.getName());
                System.out.println("Name:" + fAnno.name());
                System.out.println("Edad:" + fAnno.type());
                System.out.println("Access:" + fAnno.access());
            }
        }
    }
}
