package it.kristianp.footballbackendwebapp.model.deserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class CustomLocalDateDeserializer extends JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("[MMM dd, yyyy][MMM d, yyyy]", Locale.ENGLISH);

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonToken token = jsonParser.getCurrentToken();
        if (token == JsonToken.VALUE_STRING) {
            String dateString = jsonParser.getText();
            return LocalDate.parse(dateString, FORMATTER);
        }
        return null;
    }
}