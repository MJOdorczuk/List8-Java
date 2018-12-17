package Pack;

import GUI.MyCalendar;
import com.sun.imageio.plugins.gif.GIFImageReader;

import javax.swing.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarModel extends AbstractListModel {

    public static String DoWName(int i){
        return (new String[]{"Mon", "Tue", "Wed", "Yur", "Fri", "Sat", "Sun"})[i];
    }

    public static String MonthName(int i){
        return (new String[]{ "January", "February", "March", "April", "May",
                "June", "July", "August", "September", "October", "November", "December"})[i];
    }

    public static final int DAY_OF_WEEK = 0;
    public static final int DAY_OF_MONTH = 1;
    public static final int MONTH = 2;
    public static final int YEAR = 3;

    private final MyCalendar view;

    private int year, month;

    public CalendarModel(MyCalendar view)
    {
        this.view = view;
        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
    }

    @Override
    public int getSize() {
        GregorianCalendar lastDay = new GregorianCalendar(year, month,1);
        lastDay.add(Calendar.MONTH,1);
        lastDay.add(Calendar.DATE, -1);

        return lastDay.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public Object getElementAt(int index) {
        GregorianCalendar calendar = new GregorianCalendar(year, month, 1);
        calendar.add(Calendar.DATE, index);
        String DoW = DoWName(calendar.get(Calendar.DAY_OF_WEEK) - 1);
        String day = "" + calendar.get(Calendar.DAY_OF_MONTH);
        String month = MonthName(calendar.get(Calendar.MONTH));
        String year = "" + calendar.get(Calendar.YEAR);
        return DoW + " " + day + " " + month + " " + year;
    }

    public void addYear(int i)
    {
        if(year + i < 1) return;
        if(year + i > 9999) return;
        GregorianCalendar calendar = new GregorianCalendar(year,month,1);
        calendar.add(Calendar.YEAR,i);
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
    }

    public void setDate(int year, int month)
    {
        this.year = year < 10000 ? (year > 0 ? year : 1) : 9999;
        this.month = month % 12;
    }

    public void setMonth(int month)
    {
        this.month = month % 12;
    }

    public int getWeek(int index) {
        GregorianCalendar calendar = new GregorianCalendar(year, month, 1);
        calendar.add(Calendar.DATE, index);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }
}
