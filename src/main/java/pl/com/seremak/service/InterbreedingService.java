package pl.com.seremak.service;

import io.micronaut.runtime.context.scope.ThreadLocal;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import jakarta.inject.Singleton;
import lombok.Data;
import pl.com.seremak.model.Individual;
import pl.com.seremak.model.Population;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Data
@Singleton
public class InterbreedingService {

    private final Random random;
    private double interbreedingProbability;
    private List<Individual> parentPopulation;

    InterbreedingService() {
        this.random = new Random();
    }

    public List<Individual> performInterbreedingInPopulation(final Population population) {
        parentPopulation = List.ofAll(population.getIndividuals());
        List<Individual> childPopulation = List.empty();
        while (parentPopulation.length() > 1) {
            childPopulation = interbreedIndividualsPair(childPopulation);
        }
        childPopulation = childPopulation.appendAll(parentPopulation);
        return childPopulation;
    }

    private List<Individual> interbreedIndividualsPair(List<Individual> childPopulation) {
        var interbreedingResult =
                Stream.of(drawIndividualPair())
                        .map(this::interbreedPairOrCopyParents)
                        .flatMap(result -> result.apply(Stream::of))
                        .collect(Collectors.toList());
        return childPopulation.appendAll(interbreedingResult);
    }

    private Tuple2<Individual, Individual> interbreedPairOrCopyParents(final Tuple2<Individual, Individual> drawnPair) {
        return drawToInterbreed() ?
                interbreedPair(drawnPair) :
                drawnPair;
    }

    private Tuple2<Individual, Individual> drawIndividualPair() {
        return Tuple.of(getAndRemove(drawIndex()), getAndRemove(drawIndex()));
    }

    private Tuple2<Individual, Individual> interbreedPair(final Tuple2<Individual, Individual> individualPair) {
        final int drawCuttingPoint = drawCuttingPoint();
        final List<Boolean> child1 = individualPair._1.getIndividual().take(drawCuttingPoint)
                .appendAll(individualPair._2.getIndividual().takeRight(8 - drawCuttingPoint));
        final List<Boolean> child2 = individualPair._2.getIndividual().take(drawCuttingPoint)
                .appendAll(individualPair._1.getIndividual().takeRight(8 - drawCuttingPoint));
        return Tuple.of(Individual.of(child1), Individual.of(child2));
    }

    private Individual getAndRemove(final int index) {
        var individual = parentPopulation.get(index);
        parentPopulation = parentPopulation.removeAt(index);
        return individual;
    }

    private boolean drawToInterbreed() {
        return random.nextFloat() <= interbreedingProbability;
    }

    private int drawCuttingPoint() {
        return random.nextInt(7) + 1;
    }

    private int drawIndex() {
        return random.nextInt(parentPopulation.length());
    }
}
