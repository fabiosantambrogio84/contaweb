package utils;

import java.io.IOException;
import java.util.List;

public class CSVUtils {

    private static final char DEFAULT_SEPARATOR = ';';

    public static String writeLine(List<String> values) throws IOException {
        return writeLine(values, DEFAULT_SEPARATOR, ' ');
    }

    public static String writeLine(List<String> values, char separators) throws IOException {
        return writeLine(values, separators, ' ');
    }

    public static String writeLine(List<String> values, char separators, char customQuote) throws IOException {

        boolean first = true;

        // default customQuote is empty

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            if (!first) {
                sb.append(separators);
            }
            if (customQuote == ' ') {
                sb.append(followCVSformat(value));
            } else {
                sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
            }

            first = false;
        }
        sb.append("\n");
        return sb.toString();
    }

    public static String writeToString(List<String> rows) {
        StringBuilder sb = new StringBuilder();
        for (String row : rows) {
            sb.append(row);
        }
        return sb.toString();
    }

    // https://tools.ietf.org/html/rfc4180
    private static String followCVSformat(String value) {
        String result = value;

        if (result.indexOf(DEFAULT_SEPARATOR) >= 0) {
            result = "\"" + value + "\"";
        } else {
            if (result.contains("\"")) {
                result = result.replace("\"", "\"\"");
            }
        }
        return result;

    }
}
