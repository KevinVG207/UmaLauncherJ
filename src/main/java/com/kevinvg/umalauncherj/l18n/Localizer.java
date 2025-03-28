package com.kevinvg.umalauncherj.l18n;

import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormatSymbols;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
@Singleton
public class Localizer {
    private Locale locale = Locale.US;
    private ResourceBundle bundle;

    Localizer(){
        setLocale(this.locale);
    }

    public void setLocale(Locale locale){
        this.locale = locale;
        this.bundle = ResourceBundle.getBundle("loc/loc", locale);
    }

    public String get(String key){
        if (!bundle.containsKey(key)){
            log.warn("Missing loc string for locale {}: {}", this.locale, key);
            return key;
        }

        return bundle.getString(key);
    }

    public String turnToString(int turn) {
        turn -= 1;

        if (turn < 12) {
            turn /= 2;
            turn += 6;
        }

        boolean secondHalf = turn % 2 == 0;
        if (secondHalf) {
            turn -= 1;
        }

        turn /= 2;

        int month = turn % 12;
        int year = (turn / 12) + 1;

        String monthString;
        try {
            monthString = DateFormatSymbols.getInstance(locale).getMonths()[month];
        } catch (ArrayIndexOutOfBoundsException e) {
            monthString = "MONTH";
        }


        return get("GAME_TURN_STRING_FORMAT").formatted(year, secondHalf ? get("GAME_TURN_STRING_LATE") : get("GAME_TURN_STRING_EARLY"), monthString);
    }
}
