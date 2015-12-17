package info.kuechler.bmf.taxcalculator;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalculationUtils {

    public static String createKeyFrom(Date date) {

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);

        return String.format("%s%02d", gc.get(Calendar.YEAR), gc.get(Calendar.MONTH) + 1);
    }
}
