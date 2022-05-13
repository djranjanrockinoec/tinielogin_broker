package com.tinie.login_broker.util;

import java.io.*;

public final class StreamToStringConverter {

    public static String toString(InputStream stream) {
        StringBuilder stringBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(stream))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                stringBuilder.append((char) c);
            }
        } catch (IOException e) {
            return "";
        }

        return stringBuilder.toString();
    }
}
