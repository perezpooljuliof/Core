package mx.com.core.entity;

import mx.com.core.db.ParamType;
import mx.com.core.db.Parameter;
import mx.com.core.db.StoreProcedure;

@StoreProcedure(name = "USUARIO")
public class Usuario {
    @Parameter(name = "NOMBRE", type = ParamType.VARCHAR)
    private String nombre;
    @Parameter(name = "EDAD", type = ParamType.INT)
    private int edad;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
}
