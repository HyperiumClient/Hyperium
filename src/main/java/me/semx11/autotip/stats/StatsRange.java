package me.semx11.autotip.stats;

import me.semx11.autotip.Autotip;

import java.time.LocalDate;

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
        assert !(that instanceof StatsRange) : "Cannot merge StatsRange with StatsRange";
        assert that instanceof StatsDaily : "Cannot merge with StatsRange";
        LocalDate date = ((StatsDaily) that).date;
        assert !date.isBefore(start) && !date.isAfter(end) : "Date is not in range";
        super.merge(that);
        return this;
    }
}
