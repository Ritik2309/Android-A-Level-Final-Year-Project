package com.ritik.finalproject;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

public class CalendarViews extends Fragment {
    CalendarView Mycalendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        //sets the main layout of the activity
        View v = inflater.inflate(R.layout.fragment_calender, container, false);

        super.onCreate(savedInstanceState);
        Mycalendar = v.findViewById(R.id.calendarView);

        //initializes the calendarview
        initializeCalendar();

        return v;
    }


        public void initializeCalendar() {


            // sets whether to show the week number.
            Mycalendar.setShowWeekNumber(false);

            // sets the first day of week according to Calendar.
            // here we set Monday as the first day of the Calendar
            Mycalendar.setFirstDayOfWeek(2);

            //The background color for the selected week.
            Mycalendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));

            //sets the color for the dates of an unfocused month.
            Mycalendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));

            //sets the color for the separator line between weeks.
            Mycalendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));

            //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
            Mycalendar.setSelectedDateVerticalBar(R.color.darkgreen);

            //sets the listener to be notified upon selected date change.
            Mycalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                //show the selected date as a toast
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                    Toast.makeText(getContext(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
                }
            });
        }
    }




