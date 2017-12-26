package com.lordarian.lahelpers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    public static String gregorianToJalali(long milisec){
        milisec *= 1000;
        Date date = new Date(milisec);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String gdate = dateFormat.format(date);
        return gregorianToJalali(gdate);
    }

    public static String gregorianToJalali(String gdate){
        int[] ggMonths = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int[] jlMonths = {31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29};
        int gyear, gmonth, gday, gnum = 0, jnum, tmp, jyear, jmonth, jday;

        gyear = Integer.valueOf(gdate.substring(0, 4)) - 1600;
        gmonth = Integer.valueOf(gdate.substring(5, 7)) - 1;
        gday = Integer.valueOf(gdate.substring(8, 10)) - 1;
        gnum = 365 * gyear + (gyear + 3) / 4 - (gyear + 99) / 100 + (gyear + 399) / 400;

        for(int i = 0; i < gmonth; i++)
            gnum += ggMonths[i];
        if(gmonth > 1 && ((gyear % 4 == 0 && gyear % 100 != 0) || (gyear % 400 == 0)))
            gnum++;
        gnum += gday;
        jnum = gnum - 79;

        tmp = jnum / 12053;
        jnum = jnum % 12053;

        jyear = 979 + 33 * tmp + 4 * jnum / 1461;
        jnum %= 1461;
        if(jnum >= 366){
            jnum = (jnum - 1) % 365;
        }
        int i;
        for(i = 0; i < 11 && jnum >= jlMonths[i]; i++)
            jnum -= jlMonths[i];
        jmonth = i + 1;
        jday = jnum + 1;
        return String.format("%d/%02d/%02d", jyear, jmonth, jday);
    }

}