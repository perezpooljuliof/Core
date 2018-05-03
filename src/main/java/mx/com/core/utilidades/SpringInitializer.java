package mx.com.core.utilidades;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringInitializer {
    private static ApplicationContext context;

    static {
    	context = new ClassPathXmlApplicationContext("config/spring/demonio-configuracion.xml");
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }
}
