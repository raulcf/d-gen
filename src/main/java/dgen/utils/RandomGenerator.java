package dgen.utils;

import java.util.Random;

public class RandomGenerator extends Random {

    private Long seed;

    public RandomGenerator(Long seed) {
        super(seed);
        this.seed = seed;
    }

    public Long getSeed() { return seed; }

}
