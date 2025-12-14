package astra.core.utils;

import java.util.ArrayList;
import java.util.List;

public class AnimatedString {

    private final String string;
    private final List<String> strings;
    private int index;
    private boolean reverse;

    public AnimatedString(String text, String c1, String c2, String c3) {
        this(text, c1, c2, c3, 12);
    }

    public AnimatedString(String text, String c1, String c2, String c3, int p) {
        this.string = text;
        this.strings = new ArrayList<>();
        createFrames(c1, c2, c3, p);
        this.index = 0;
        this.reverse = false;
    }

    private void createFrames(String c1, String c2, String c3, int p) {
        if (string != null && !string.isEmpty()) {
            for (int i = 0; i < string.length(); i++) {
                if (string.charAt(i) != ' ') {
                    strings.add(c1 + string.substring(0, i) + c2 + string.charAt(i) + c3 + string.substring(i + 1));
                }
            }

            for (int i = 0; i < p; i++) {
                strings.add(c1 + string);
            }

            for (int i = 0; i < string.length(); i++) {
                if (string.charAt(i) != ' ') {
                    strings.add(c3 + string.substring(0, i) + c2 + string.charAt(i) + c1 + string.substring(i + 1));
                }
            }

            for (int i = 0; i < p; i++) {
                strings.add(c3 + string);
            }
        }
    }

    public String next() {
        if (strings.isEmpty()) {
            return "";
        }

        String result = strings.get(index);

        if (reverse) {
            index--;
            if (index < 0) {
                reverse = false;
                index = 1;
            }
        } else {
            index++;
            if (index >= strings.size()) {
                reverse = true;
                index = strings.size() - 2;
            }
        }

        return result;
    }
}
