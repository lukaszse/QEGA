package pl.com.seremak.model;

import io.micronaut.runtime.context.scope.ThreadLocal;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@ThreadLocal
public class Population {

    private List<Individual> individuals;

    public void generatePopulation(final int individualsNumber) {
        this.individuals = Stream.range(0, individualsNumber)
                .map(i -> new Individual())
                .collect(List.collector());
    }

    public List<Integer> toIntegerList() {
        return toIntegerList(individuals);
    }

    public static List<Integer> toIntegerList(final List<Individual> individuals) {
        return individuals
                .map(Individual::toInt);
    }
}
