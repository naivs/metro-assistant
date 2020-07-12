package org.naivs.perimeter.metro.assistant.data.model.rest;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.Getter;

@Data
public class Stock {

    public enum Status {
        ENDS("Заканчивается", 25),
        FEW("Мало", 50),
        ENOUGH("Достаточно", 75),
        LOT("Много", 100);

        private String text;
        @Getter
        private Integer pct;

        Status(String text, Integer pct) {
            this.text = text;
            this.pct = pct;
        }

        @JsonValue
        public String getText() {
            return text;
        }
    }

    private Integer value;
    private Status text;
}
