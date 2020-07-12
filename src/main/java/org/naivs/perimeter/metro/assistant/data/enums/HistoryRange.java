package org.naivs.perimeter.metro.assistant.data.enums;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
public enum HistoryRange {

    WEEK {
        @Override
        public LocalDateTime get() {
            return now().minusDays(7L);
        }
    },
    MONTH {
        @Override
        public LocalDateTime get() {
            return now().minusMonths(1L);
        }
    },
    QUARTER {
        @Override
        public LocalDateTime get() {
            return now().minusMonths(3L);
        }
    },
    YEAR {
        @Override
        public LocalDateTime get() {
            return now().minusYears(1L);
        }
    };

    public abstract LocalDateTime get();

    protected LocalDateTime now() {
        return LocalDateTime.now(ZoneId.of("+3"));
    }
}
