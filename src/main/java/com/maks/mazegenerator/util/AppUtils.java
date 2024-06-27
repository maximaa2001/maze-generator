package com.maks.mazegenerator.util;

public final class AppUtils {

    public static String generatePaneData(int row, int column) {
        return String.format("%s_%s", row, column);
    }

    public static int resolvePaneRow(String data) {
        return Integer.parseInt(data.split("_")[0]);
    }

    public static int resolvePaneColumn(String data) {
        return Integer.parseInt(data.split("_")[1]);
    }
}
