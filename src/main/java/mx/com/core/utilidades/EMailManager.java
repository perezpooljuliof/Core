package mx.com.core.utilidades;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EMailManager {
    private static Session session;
    private static Properties props = new Properties();

    static {
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("pruebasjfpp@gmail.com", "PruebasDimex");
            }
        });
    }

    public static void sendMail() throws MessagingException, UnsupportedEncodingException {
        Message msg = new MimeMessage(session);
        msg.setSubject("Mensaje de prueba.");
        msg.setFrom(new InternetAddress("perezpooljuliof@gmail.com","Elver"));
        msg.addRecipients(Message.RecipientType.TO, new InternetAddress[] { new InternetAddress("perezpooljuliof@gmail.com") });
        msg.setText("Hola mundo!!");
        javax.mail.Transport.send(msg);
        System.out.println("Mensaje enviado correctamente!!");
    }

    public static void main(String[] args) {
        try {
            EMailManager.sendMail();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
