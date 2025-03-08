package com.kevinvg.umalauncherj.util;

import com.kevinvg.umalauncherj.Constants;

import static com.kevinvg.umalauncherj.Constants.MONTH_MAP;

public class GameUtil {
    private GameUtil() {}

    public static String turnToString(int turn) {
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

        int month = turn % 12 + 1;
        int year = (turn / 12) + 1;

        return "Y%d, %s %s".formatted(year, secondHalf ? "Late" : "Early", MONTH_MAP.getOrDefault(month, "MONTH"));
    }
}
