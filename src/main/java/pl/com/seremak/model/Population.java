package pl.com.seremak.model;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class Population {

    private List<Individual> individuals;

    public Population(final int individualsNumber) {
        this.individuals = generatePopulation(individualsNumber);
    }

    private static List<Individual> generatePopulation(final int individualsNumber) {
         return IntStream.range(0, individualsNumber)
                .mapToObj(i -> new Individual())
                 .collect(Collectors.toList());
    }
}
