package pl.com.seremak.model;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Random;

@Data
@AllArgsConstructor(staticName = "of")
public class Individual {

    private List<Boolean> individual;

    public Individual() {
        this.individual = generateIndividual();
    }

    public int toInt() {
        return toInt(individual);
    }

    public int toInt(final List<Boolean> individual) {
        return Stream.range(0, 8)
                .map(i -> booleanToInt(individual.get(7-i))* (int) Math.pow(2, i))
                .sum().intValue();
    }

    private List<Boolean> generateIndividual() {
        var random = new Random();
        return List.range(0, 8)
                .map(i -> random.nextBoolean());
    }

    private int booleanToInt(final boolean bool) {
        return bool ? 1 : 0;
    }
}
