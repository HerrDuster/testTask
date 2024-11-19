package com.spribe.test.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

public class TimestampToDateDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
        long timestamp = parser.getValueAsLong();
        return Date.from(Instant.ofEpochSecond(timestamp));
    }

}
