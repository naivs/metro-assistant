package org.naivs.perimeter.metro.assistant.data.model;

import lombok.Data;
import org.naivs.perimeter.metro.assistant.data.enums.NotificationType;

import java.time.LocalDateTime;

@Data
public class Notification {

    private NotificationType type;
    private LocalDateTime when = LocalDateTime.now();
    private Object[] args;

    public String toString() {
        if (args.length != type.getArgs()) {
            throw new RuntimeException(
                    String.format("Can't Notification toString(). Expected args: %d, but got: %d",
                            type.getArgs(), args.length));
        }
        return String.format(type.getTemplate(), args);
    }
}
