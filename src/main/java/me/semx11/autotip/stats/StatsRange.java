package me.semx11.autotip.stats;

import java.time.LocalDate;
import me.semx11.autotip.Autotip;

public class StatsRange extends Stats {

    private final LocalDate start;
    private final LocalDate end;

    public StatsRange(Autotip autotip, LocalDate start, LocalDate end) {
        super(autotip);
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date is after end date");
        }
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public String getStartString() {
        return DATE_FORMATTER.format(start);
    }

    public String getEndString() {
        return DATE_FORMATTER.format(end);
    }


    @Override
    public StatsRange merge(final Stats that) {
        if (that instanceof StatsRange) {
            throw new UnsupportedOperationException("Cannot merge StatsRange with StatsRange");
        }
        if (!(that instanceof StatsDaily)) {
            throw new IllegalArgumentException("Cannot merge with StatsRange");
        }
        LocalDate date = ((StatsDaily) that).date;
        if (date.isBefore(start) || date.isAfter(end)) {
            throw new IllegalArgumentException("Date is not in range");
        }
        super.merge(that);
        return this;
    }

}
