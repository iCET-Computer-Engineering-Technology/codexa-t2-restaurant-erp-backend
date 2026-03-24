package edu.icet.ecom.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class FlexibleLocalTimeDeserializer extends StdDeserializer<LocalTime> {

    private static final DateTimeFormatter TIME_24H_WITH_SECONDS = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter TIME_24H_WITHOUT_SECONDS = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter TIME_12H_WITH_SPACE = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
    private static final DateTimeFormatter TIME_12H_WITHOUT_SPACE = DateTimeFormatter.ofPattern("hhmma", Locale.ENGLISH);
    private static final DateTimeFormatter TIME_12H_WITH_SECONDS = DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.ENGLISH);

    public FlexibleLocalTimeDeserializer() {
        super(LocalTime.class);
    }

    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String timeString = p.getValueAsString();
        if (timeString == null || timeString.trim().isEmpty()) {
            return null;
        }

        timeString = timeString.trim();

        try {
            return LocalTime.parse(timeString, TIME_24H_WITH_SECONDS);
        } catch (DateTimeParseException ignored) {}

        try {
            return LocalTime.parse(timeString, TIME_24H_WITHOUT_SECONDS);
        } catch (DateTimeParseException ignored) {}

        try {
            return LocalTime.parse(timeString, TIME_12H_WITH_SPACE);
        } catch (DateTimeParseException ignored) {}

        try {
            return LocalTime.parse(timeString, TIME_12H_WITH_SECONDS);
        } catch (DateTimeParseException ignored) {}

        try {
            return LocalTime.parse(timeString, TIME_12H_WITHOUT_SPACE);
        } catch (DateTimeParseException e) {
            throw new IOException(String.format(
                    "Unable to parse time '%s'. Expected formats: HH:mm, HH:mm:ss, hh:mm AM/PM, or hh:mm:ss AM/PM", timeString), e);
        }
    }
}



