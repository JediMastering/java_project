package com.example.first.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public final class FileNameUtils {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\s]");

    private FileNameUtils() {}

    public static String sanitize(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "unnamed-file";
        }
        String nowhitespace = WHITESPACE.matcher(filename).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}