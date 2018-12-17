package GUI;

import Pack.CalendarModel;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MyCalendar {
    private JTabbedPane CalendarTabsPanel;
    private JPanel mainPanel;
    private JToolBar DateControlBar;
    private JButton DecrementYearButton;
    private JSpinner MonthSpinner;
    private JButton IncrementYearButton;
    private JSpinner YearSpinner;
    private JScrollBar YearScrollBar;
    private JPanel yearPanel;
    private JPanel monthPanel;
    private final Pack.CalendarModel model;
    private boolean valuesChanged = false;

    public boolean ValuesChanged(){return valuesChanged;}

    private String[] DoWNames = {" Mon", "Tue", "Wed", "Yur", "Fri", "Sat", "Sun"};

    private String[] MonthNames = { "January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November", "December"};

    private void setYearPanel()
    {
        yearPanel.removeAll();
        for(int i = 0; i < 12; i++)
        {
            yearPanel.add(getGridMonthPanel(i));
        }
    }

    private JPanel getGridMonthPanel(int month)
    {
        JPanel monthPanel = new JPanel();
        model.setMonth(month);
        String monthName = CalendarModel.MonthName(month);
        String[] date = model.getElementAt(0).toString().split(" ");
        int shift = 0;
        while(!CalendarModel.DoWName(shift).equals(date[CalendarModel.DAY_OF_WEEK])) shift++;

        JLabel nameLabel = new JLabel(date[CalendarModel.MONTH]);

        monthPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        monthPanel.setLayout(new BorderLayout());
        monthPanel.add(nameLabel,BorderLayout.PAGE_START);

        JPanel dayTablePanel = new JPanel();
        monthPanel.add(dayTablePanel,BorderLayout.CENTER);
        dayTablePanel.setLayout(new GridLayout(7,8));
        JLabel added = new JLabel("week",SwingConstants.CENTER);
        dayTablePanel.add(added);

        for(int i = 0; i < 7; i++)
        {
            added = new JLabel(DoWNames[i],SwingConstants.RIGHT);
            if(i == 6) added.setForeground(Color.RED);
            dayTablePanel.add(added);
        }

        for(int i = 0; i < 6; i++)
        {

            added = new JLabel("" + model.getWeek(7*(1+i) - shift), SwingConstants.CENTER);
            added.setForeground(Color.GRAY);
            dayTablePanel.add(added);
            for(int j = 0; j < 7; j++)
            {
                date = model.getElementAt(1 + i*7 + j - shift).toString().split(" ");
                added = new JLabel("" + date[CalendarModel.DAY_OF_MONTH],SwingConstants.RIGHT);
                if(!date[CalendarModel.MONTH].equals(monthName))
                {
                    if(j == 6) added.setForeground(Color.PINK);
                    else added.setForeground(Color.GRAY);
                }
                else
                {
                    if(j == 6) added.setForeground(Color.RED);
                    else added.setForeground(Color.BLACK);
                }
                dayTablePanel.add(added);
            }
        }
        return monthPanel;
    }

    private void setMonthPanel()
    {
        monthPanel.removeAll();

        JList jPrevious, jCurrent, jNext;
        ArrayList<JLabel> previous, current, next;
        previous = new ArrayList<>();
        current = new ArrayList<>();
        next = new ArrayList<>();

        JLabel added;

        String[] date = model.getElementAt(0).toString().split(" ");

        int currentMonth = 0;
        while(!CalendarModel.MonthName(currentMonth).equals(date[CalendarModel.MONTH])) currentMonth++;

        if(currentMonth == 0)
        {
            model.addYear(-1);
            model.setMonth(11);
        }
        else model.setMonth(currentMonth - 1);

        for(int i = 0; i < model.getSize(); i++)
        {
            date = model.getElementAt(i).toString().split(" ");
            added = new JLabel(date[CalendarModel.DAY_OF_MONTH] + " " + date[CalendarModel.DAY_OF_WEEK]);
            if(date[CalendarModel.DAY_OF_WEEK].equals(CalendarModel.DoWName(6))) added.setForeground(Color.PINK);
            else added.setForeground(Color.GRAY);
            previous.add(added);
        }

        model.setMonth(currentMonth);

        for(int i = 0; i < model.getSize(); i++)
        {
            date = model.getElementAt(i).toString().split(" ");
            added = new JLabel(date[CalendarModel.DAY_OF_MONTH] + " " + date[CalendarModel.DAY_OF_WEEK]);
            if(date[CalendarModel.DAY_OF_WEEK].equals(CalendarModel.DoWName(6))) added.setForeground(Color.RED);
            else added.setForeground(Color.BLACK);
            current.add(added);
        }

        if(currentMonth == 11)
        {
            model.addYear(1);
            model.setMonth(0);
        }

        model.setMonth((currentMonth + 1) % 12);

        for(int i = 0; i < model.getSize(); i++)
        {
            date = model.getElementAt(i).toString().split(" ");
            added = new JLabel(date[CalendarModel.DAY_OF_MONTH] + " " + date[CalendarModel.DAY_OF_WEEK]);
            if(date[CalendarModel.DAY_OF_WEEK].equals(CalendarModel.DoWName(6))) added.setForeground(Color.PINK);
            else added.setForeground(Color.GRAY);
            next.add(added);
        }

        jPrevious = new JList(previous.toArray());
        jPrevious.setCellRenderer(new JLabelCellRenderer());
        jCurrent = new JList(current.toArray());
        jCurrent.setCellRenderer(new JLabelCellRenderer());
        jNext = new JList(next.toArray());
        jNext.setCellRenderer(new JLabelCellRenderer());

        monthPanel.add(jPrevious);
        monthPanel.add(jCurrent);
        monthPanel.add(jNext);


    }

    public MyCalendar(JFrame frame)
    {
        model = new CalendarModel(this);
        frame.setContentPane(mainPanel);

        String[] date = model.getElementAt(0).toString().split(" ");

        int year = Integer.parseInt(date[CalendarModel.YEAR]);
        int month = 0;
        while(!CalendarModel.MonthName(month).equals(date[CalendarModel.MONTH])) month++;

        YearSpinner.setValue(year);
        MonthSpinner.setValue(month);
        YearScrollBar.setValue(year);

        monthPanel = new JPanel();
        yearPanel = new JPanel();
        CalendarTabsPanel.add(yearPanel,date[CalendarModel.YEAR]);
        CalendarTabsPanel.add(monthPanel,date[CalendarModel.MONTH]);
        yearPanel.setLayout(new GridLayout(3,4));
        monthPanel.setLayout(new GridLayout(1,3));

        YearScrollBar.setMinimum(0);
        YearScrollBar.setMaximum(9999);

        DecrementYearButton.addActionListener(e -> {
            model.addYear(-1);
            fireContentsChanged();
        });
        IncrementYearButton.addActionListener(e -> {
            model.addYear(1);
            fireContentsChanged();
        });
        YearSpinner.addChangeListener(e -> {
            if(e.getSource() instanceof MyCalendar) return;
            int y = (int) YearSpinner.getValue();
            int m = (int) MonthSpinner.getValue();
            if(y > 9999)
            {
                YearSpinner.setValue(9999);
            }
            else if(y < 0)
            {
                YearSpinner.setValue(0);
            }
            else model.setDate(y,m);
            if(!valuesChanged) fireContentsChanged();
        });
        MonthSpinner.addChangeListener(e -> {
            if(e.getSource() instanceof MyCalendar) return;
            int y = (int) YearSpinner.getValue();
            int m = (int) MonthSpinner.getValue();
            if(m > 11)
            {
                if(y < 9999) model.setDate(y + 1, 0);
                else model.setDate(y,11);
            }
            else if(m < 0)
            {
                if(y > 1) model.setDate(y - 1, 11);
                else model.setDate(y,0);
            }
            else model.setDate(y,m);
            if(!valuesChanged) fireContentsChanged();
        });
        YearScrollBar.addAdjustmentListener(e -> {
            if(e.getSource() instanceof MyCalendar) return;
            int y = YearScrollBar.getValue();
            int m = (int) MonthSpinner.getValue();
            model.setDate(y,m);
            if(!valuesChanged) fireContentsChanged();
        });

        fireContentsChanged();

    }

    public void fireContentsChanged()
    {
        valuesChanged = true;
        String[] date = model.getElementAt(0).toString().split(" ");
        int year = Integer.parseInt(date[CalendarModel.YEAR]);
        int month = 0;
        while(!CalendarModel.MonthName(month).equals(date[CalendarModel.MONTH])) month++;
        CalendarTabsPanel.setTitleAt(0, date[CalendarModel.YEAR]);
        CalendarTabsPanel.setTitleAt(1, date[CalendarModel.MONTH]);
        setMonthPanel();
        setYearPanel();
        YearSpinner.setValue(year);
        MonthSpinner.setValue(month);
        YearScrollBar.setValue(year);
        valuesChanged = false;
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("MyCalendar");

        MyCalendar calendar = new MyCalendar(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(1280,720);
    }
}
