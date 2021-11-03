package pl.com.seremak.model;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import lombok.Getter;

@Getter
public class Population {

    private final List<Individual> individuals;

    Population(final int individualsNumber) {
        this.individuals = generatePopulation(individualsNumber);
    }

    private static List<Individual> generatePopulation(final int individualsNumber) {
         return Stream.range(0, individualsNumber)
                .map(i -> new Individual())
                 .collect(List.collector());
    }
}
