package mx.com.core.utilidades;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    /**
     * Funcion que devuelva la fecha del anio, mes y dia proporcionados.
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date getDate(int year, int month, int day) {
        month = month - 1;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        return calendar.getTime();
    }

    /**
     * Funcion que devuelve un String con la fecha con el formato
     * proporcionado
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToString(Date date, String format) {
        SimpleDateFormat formater = new SimpleDateFormat(format);
        return formater.format(date);
    }

    /**
     * Funcion que devuelve la ultima fecha del mes.
     *
     * @param date
     * @return
     */
    public static Date getLastMonthDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return calendar.getTime();
    }

    public static void main(String[] args) {
        Date fecha = getDate(2016, 2, 1);
        Date ultimoDiaMes = getLastMonthDate(fecha);
        System.out.println(dateToString(fecha, "yyyy/MM/dd"));
        System.out.println(dateToString(ultimoDiaMes, "yyyy/MM/dd"));
    }
}
