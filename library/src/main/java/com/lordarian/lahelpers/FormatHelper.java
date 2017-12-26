package com.lordarian.lahelpers;

import android.text.TextUtils;

public class FormatHelper {

    private static String[] persianNumbers = new String[]{ "۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹" };

    private static String[] persianNumberWords = new String[]{"صفر", "یک", "دو", "سه", "چهار", "پنج", "شش", "هفت", "هشت", "نه",
            "صفر", "ده", "بیست", "سی", "چهل", "پنجاه", "شصت", "هفتاد", "هشتاد", "نود",
            "صفر", "صد", "دویست", "سیصد", "چهارصد", "پانصد", "ششصد", "هفتصد", "هشتصد", "نهصد",
            "ده", "یازده", "دوازده", "سیزده", "چهارده", "پانزده", "شانزده", "هفده", "هجده", "نوزده"};

    public static boolean isPersian(String s){
        for(int i = 0; i < s.length(); i++)
            if(s.charAt(i) < '۰' || s.charAt(i) > '۹')
                return false;
        return true;
    }

    public static String toPersianNumber(String text) {
        if (text.length() == 0) {
            return "";
        }
        String out = "";
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if ('0' <= c && c <= '9') {
                int number = Integer.parseInt(String.valueOf(c));
                out += persianNumbers[number];
            } else if (c == '٫') {
                out += '،';
            } else {
                out += c;
            }
        }
        return out;
    }

    public static String toPersianNumber(String text, boolean separate){
        if(!separate)
            return toPersianNumber(text);
        int j = 0;
        for(int i = text.length() - 1; i >= 0; i--){
            j++;
            if(j == 3 && i > 0) {
                text = text.substring(0, i) + "," + text.substring(i, text.length());
                j = 0;
            }
        }
        return toPersianNumber(text);
    }

    public static String toEnglishNumber(String text) {
        if (text.length() == 0) {
            return "";
        }
        String out = "";
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            for(int j = 0; j < 10; j++) {
                if (c == persianNumbers[j].charAt(0)) {
                    char ch = (char) ('0' + j);
                    String str = String.valueOf(ch);
                    out = out.concat(str);
                } else if (c == '،') {
                    out += '٫';
                }
            }
        }
        return out;
    }

    public static String toPersianWord(int num) {
        String s = String.format("%03d",num);
        if(s.length() > 3)
            return toPersianNumber(s,true);
        String str = "";
        for(int i = 0; i < 3; i++){
            if(s.charAt(i) != '0'){
                if(str.length() != 0)
                    str = str.concat(" و ");
                if(i==1 && s.charAt(1) == '1'){
                    return str.concat(persianNumberWords[30 + Integer.valueOf(s.charAt(2) - '0')]);
                }
                str = str.concat(persianNumberWords[10 * (2 - i) + Integer.valueOf(s.charAt(i) - '0')]);
            }
        }
        return str;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null || TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static String timeFormat(int minutes, int seconds) {
        return toPersianNumber(String.format("%d:%02d", minutes, seconds));
    }
}