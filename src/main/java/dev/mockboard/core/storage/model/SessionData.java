package dev.mockboard.core.storage.model;

import lombok.Data;
import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class SessionData implements Serializable {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final BoardData boardData = new BoardData();

    @Getter
    private final AtomicLong lastAccessTime = new AtomicLong(System.currentTimeMillis());

    public void touchLastAccessTime() {
        lastAccessTime.set(System.currentTimeMillis());
    }

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
    }

    public void removeEmitter(SseEmitter emitter) {
        emitters.remove(emitter);
    }
}
