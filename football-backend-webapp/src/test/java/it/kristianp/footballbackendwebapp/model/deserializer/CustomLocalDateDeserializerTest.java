package it.kristianp.footballbackendwebapp.model.deserializer;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

class CustomLocalDateDeserializerTest {

    @Test
    void test() {
        String dateString = "May 15, 1992";

        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);

        try {
            Date date = formatter.parse(dateString);
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            System.out.println(localDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test2() {
        String dateString = "Jun 28, 1994";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);

        LocalDate localDate = LocalDate.parse(dateString, formatter);

        System.out.println(localDate);
    }
}