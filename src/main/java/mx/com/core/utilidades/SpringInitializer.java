package mx.com.core.utilidades;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringInitializer {
    private static ApplicationContext context;

    /*
    static {
    	context = new ClassPathXmlApplicationContext("config/spring/config.xml");
    }
    */

    public static ApplicationContext getApplicationContext() {
        return context;
    }
}
