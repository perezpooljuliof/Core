package mx.com.core.db.param;

import mx.com.core.db.base.TipoLlamada;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Parameter {
    public String name() default "";
    public ParamType type() default ParamType.VARCHAR;
    public ParamAccess access() default ParamAccess.IN;
    public TipoLlamada[] targets() default {TipoLlamada.ALTA, TipoLlamada.BAJA, TipoLlamada.MODIFICACION, TipoLlamada.PROCESO};
}
