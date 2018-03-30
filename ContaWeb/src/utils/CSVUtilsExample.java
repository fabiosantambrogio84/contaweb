package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVUtilsExample {

    public static void main(String[] args) throws Exception {

        // String csvFile = "/Users/mkyong/csv/abc.csv";
        // FileWriter writer = new FileWriter(csvFile);
        List<String> rows = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String row = CSVUtils.writeLine(Arrays.asList("a;" + i, "b" + i, "c" + i, "d" + i));
            rows.add(row);
        }
        System.out.println(CSVUtils.writeToString(rows));

        // custom separator + quote
        // CSVUtils.writeLine(Arrays.asList("aaa", "bb,b", "cc,c"), ',', '"');
        //
        // // custom separator + quote
        // CSVUtils.writeLine(Arrays.asList("aaa", "bbb", "cc,c"), '|', '\'');
        //
        // // double-quotes
        // CSVUtils.writeLine(Arrays.asList("aaa", "bbb", "cc\"c"));

    }

}
