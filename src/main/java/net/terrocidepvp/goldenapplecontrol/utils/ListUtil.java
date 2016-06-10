package net.terrocidepvp.goldenapplecontrol.utils;

import java.util.List;

public class ListUtil {

    public static boolean doesListContain(List<String> list, String checkAgainst) {
        for (String str : list) {
            if (str.equals(checkAgainst)) {
                return true;
            }
        }
        return false;
    }

}
