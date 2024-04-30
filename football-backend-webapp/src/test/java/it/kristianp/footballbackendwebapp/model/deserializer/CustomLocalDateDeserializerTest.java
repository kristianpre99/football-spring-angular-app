package it.kristianp.footballbackendwebapp.model.deserializer;

import org.junit.jupiter.api.Assertions;
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
    void test() throws ParseException {
        String dateString = "May 15, 1992";
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        Date date = formatter.parse(dateString);
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        String expectedValue = "1992-05-15";
        Assertions.assertNotNull(localDate);
        Assertions.assertEquals(localDate.toString(), expectedValue);
        Assertions.assertEquals(1992, localDate.getYear());
        Assertions.assertEquals(5, localDate.getMonthValue());
        Assertions.assertEquals(15, localDate.getDayOfMonth());
    }

    @Test
    void test2() {
        String dateString = "Jun 28, 1994";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
        LocalDate localDate = LocalDate.parse(dateString, formatter);

        String expectedValue = "1994-06-28";
        Assertions.assertNotNull(localDate);
        Assertions.assertEquals(localDate.toString(), expectedValue);
        Assertions.assertEquals(1994, localDate.getYear());
        Assertions.assertEquals(6, localDate.getMonthValue());
        Assertions.assertEquals(28, localDate.getDayOfMonth());
    }
}