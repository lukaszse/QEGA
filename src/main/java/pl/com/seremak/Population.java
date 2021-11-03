package pl.com.seremak;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class Population {

    private List<Individual> individuals;

    Population(final int individualsNumber) {
        this.individuals = generatePopulation(individualsNumber);
    }

    private static List<Individual> generatePopulation(final int individualsNumber) {
        return IntStream.range(0, individualsNumber)
                .mapToObj(i -> new Individual())
                .collect(Collectors.toList());
    }
}
