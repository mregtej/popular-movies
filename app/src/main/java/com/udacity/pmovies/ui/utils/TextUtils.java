package com.udacity.pmovies.ui.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextUtils {

    public static String convertListOfIntToString(List<Integer> arrayList) {
        StringBuilder sb = new StringBuilder();
        for (int i = arrayList.size() - 1; i >= 0; i--) {
            int num = arrayList.get(i);
            sb.append(num);
            sb.append(",");
        }
        return sb.toString();
    }

    public static List<Integer> convertCsvStringToListOfInt(String csv_string) {
        List<String> arrayStrings = Arrays.asList(csv_string.split("\\s*,\\s*"));
        List<Integer> arrayInts = new ArrayList<Integer>(arrayStrings.size());
        for(String s : arrayStrings) {
            arrayInts.add(Integer.parseInt(s));
        }
        return arrayInts;
    }
}
