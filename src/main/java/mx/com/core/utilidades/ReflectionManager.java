package mx.com.core.utilidades;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionManager {
    public static ReflectionManager instance;

    private ReflectionManager() {
        instance = new ReflectionManager();
    }

    public ReflectionManager getInstance() {
        return instance;
    }



    public static List<Field> getFields(Class clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    public static List<Field> getFields(Object object) {
        Class clazz = object.getClass();
        return getFields(clazz);
    }

    public static Method getMethod(Object instance, String methodName, Class... parametersType) {
        Class classType = instance.getClass();
        return getMethod(classType, methodName, parametersType);
    }

    /**
     * Funcion para obtener las anotaciones correspondientes a la clase del objeto.
     *
     * @param object
     * @return
     */
    public static Annotation[] getAnnotations(Object object) {
        Class clazz = object.getClass();
        return getAnnotations(clazz);
    }

    /**
     * Funcion para obtener las anotaciones de una clase.
     *
     * @param clazz
     * @return
     */
    public static Annotation[] getAnnotations(Class clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        return annotations;
    }

    /**
     * Funcion para obtener las anotaciones de una variable.
     *
     * @param field
     * @return
     */
    public static Annotation[] getAnnotations(Field field) {
        Annotation[] annotations = field.getDeclaredAnnotations();
        return annotations;
    }

    public static boolean isAnnotationPresent(Field field, Class clazz) {
        return field.isAnnotationPresent(clazz);
    }

    public static Annotation getAnnotation(Class clazz, Class annotationClass) {
        return clazz.getAnnotation(annotationClass);
    }

    /**
     * Funcion para devolver la anotacion de una variable.
     * Devuelve la referencia de la anotacion.
     * @param field
     * @param annotationClass
     * @return
     */
    public static Object getAnnotation(Field field, Class annotationClass) {
        return field.getAnnotation(annotationClass);
    }

    /**
     *
     * @param classType
     * @param methodName
     * @param parametersType
     * @return
     */
    public static Method getMethod(Class classType, String methodName, Class... parametersType)  {
        Method metodo = null;
        try {
            metodo = classType.getDeclaredMethod(methodName, parametersType);
        }
        catch (Exception e) {

        }

        return metodo;
    }

    /**
     *
     * @param instance
     * @param method
     * @param args
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object getMethodValue(Object instance, Method method, Object... args)
            throws InvocationTargetException, IllegalAccessException {
        return method.invoke(instance, args);
    }

    /**
     * Retrieve a value from a property using
     *
     * @param obj The object who's property you want to fetch
     * @param property The property name
     * @param defaultValue A default value to be returned if either a) The property is
     *  not found or b) if the property is found but the value is null
     * @return THe value of the property
     */
    public static <T> T getProperty(Object obj, String property, T defaultValue) {

        T returnValue = (T) getProperty(obj, property);
        if (returnValue == null) {
            returnValue = defaultValue;
        }

        return returnValue;
    }

    /**
     * Fetch a property from an object. For example of you wanted to get the foo
     * property on a bar object you would normally call {@code bar.getFoo()}. This
     * method lets you call it like {@code BeanUtil.getProperty(bar, "foo")}
     * @param obj The object who's property you want to fetch
     * @param property The property name
     * @return The value of the property or null if it does not exist.
     */
    public static Object getProperty(Object obj, String property) {
        Object returnValue = null;

        try {
            String methodName = "get" + property.substring(0, 1).toUpperCase() + property.substring(1, property.length());
            Class clazz = obj.getClass();
            Method method = clazz.getMethod(methodName, null);
            returnValue = method.invoke(obj, null);
        }
        catch (Exception e) {
            // Do nothing, we'll return the default value
        }

        return returnValue;
    }

    /*
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        Empleado empleado = new Empleado();
        empleado.setId(4);

        //Object value = getMethodValue(empleado, metodo, null);
        //Object value2 = getProperty(empleado, "id");

        //System.out.println(metodo + " / " + value + " / " + value2);
    }
    */
}
