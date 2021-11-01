package pl.com.seremak;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Data
public class Individual {

    private List<Boolean> individual;

    Individual() {
        this.individual = generateIndividual();
    }

    public int toInt() {
        return IntStream.range(0, 8)
                .map(i -> booleanToInt(individual.get(7-i))* (int) Math.pow(2, i))
                .sum();
    }

    private List<Boolean> generateIndividual() {
        var individual = new ArrayList<Boolean>();
        var random = new Random();
        IntStream.range(0, 8)
                .forEach( i -> individual.add(random.nextBoolean()));
        return individual;
    }

    private int booleanToInt(final boolean bool) {
        return bool ? 1 : 0;
    }
}
