package dev.mockboard.core.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private EventType type;
    private Object data;
    private Long timestamp;

    public static Event of(EventType type, Object data) {
        return Event.builder()
                .type(type)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
