package org.naivs.perimeter.metro.assistant.data.model.external;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.Getter;

@Data
public class Stock {

    public enum Status {
        OUT("Отсутствует", 0),
        ENDS("Заканчивается", 25),
        FEW("Товара мало", 50),
        ENOUGH("Товара достаточно", 75),
        LOT("Товара много", 100);

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
