package com.hcc.handlers.handlers;

import com.hcc.config.ConfigOpt;

/**
 * Created by mitchellkatz on 2/14/18. Designed for production use on Sk1er.club
 */
public class ValueHandler {
    @ConfigOpt
    private int rankedRating;
    @ConfigOpt
    private int deltaRankedRating;

    public int getDeltaRankedRating() {
        return deltaRankedRating;
    }

    public void setDeltaRankedRating(int deltaRankedRating) {
        this.deltaRankedRating = deltaRankedRating;
    }

    public int getRankedRating() {
        return rankedRating;
    }

    public void setRankedRating(int rankedRating) {
        this.rankedRating = rankedRating;
    }
}
