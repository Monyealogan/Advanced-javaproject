package edu.wgu.d387_sample_code.convertor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Setter
@Getter
public class TimeServiceImpl implements TimeService {
    private List<LocalDateTime> times = new ArrayList<>();

    @Override
    public void addTimeZones() {
        ZoneId eastern = ZoneId.of("America/New_York");
        ZoneId mountain = ZoneId.of("America/Denver");
        ZoneId utc = ZoneId.of("UTC");
        ZoneId zoneId = ZoneId.systemDefault();

        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);

        times.clear();
        times.add(zonedDateTime.withZoneSameInstant(eastern).toLocalDateTime());
        times.add(zonedDateTime.withZoneSameInstant(mountain).toLocalDateTime());
        times.add(zonedDateTime.withZoneSameInstant(utc).toLocalDateTime());
    }

    @Override
    public String timesToJson() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            return mapper.writeValueAsString(times);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{\"error\":\"Failed to serialize times\"}";
        }
    }
}
