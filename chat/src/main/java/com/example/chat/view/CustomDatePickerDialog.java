package com.example.chat.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/27      14:15
 * QQ:             1981367757
 */

public class CustomDatePickerDialog extends DatePickerDialog {


        private int maxYear = 2016;
        private int minYear = 1900;
        private int currentYear;
        private int currentMonth;
        private int currentDay;


        public CustomDatePickerDialog(Context context, OnDateSetListener listener, int year, int month, int dayOfMonth) {
                super(context, listener, year, month, dayOfMonth);
                currentDay = dayOfMonth;
                currentMonth = month;
                currentYear = year;
        }


        @Override
        public void onDateChanged(DatePicker view, int year, int month, int dayOfMonth) {
                if (year > minYear && year < maxYear) {
                        currentYear = year;
                        currentMonth = month;
                        currentDay = dayOfMonth;
                } else if (currentYear < minYear) {
                        currentYear = minYear;
                } else {
                        currentYear = maxYear;
                }
                super.onDateChanged(view, currentYear, currentMonth, currentDay);
        }

        public void setMaxYear(int maxYear) {
                this.maxYear = maxYear;
        }

        public void setMinYear(int minYear) {
                this.minYear = minYear;
        }
}
