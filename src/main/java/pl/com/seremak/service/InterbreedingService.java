package pl.com.seremak.service;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import pl.com.seremak.model.Individual;
import pl.com.seremak.model.Population;

import java.util.Random;

public class InterbreedingService {

    private static final int DRAW_RANGE = 1000000;

    private List<Individual> parentPopulation;
    private List<Individual> childPopulation;
    private final double interbreedingProbability;
    private final Random random;

    InterbreedingService(final Population parentPopulation, final double interbreedingProbability) {
        this.parentPopulation = List.ofAll(parentPopulation.getIndividuals());
        this.interbreedingProbability = interbreedingProbability;
        this.childPopulation = List.empty();
        this.random = new Random();
    }


    public List<Individual> performInterbreedingInPopulation() {
        while (parentPopulation.length() > 1) {
            var drawnPair = drawIndividualPair();
            var interbreedingResult = drawIfInterbreed() ?
                    interbreedPair(drawnPair).apply(List::of) :
                    drawnPair.apply(List::of);
            childPopulation = childPopulation.appendAll(interbreedingResult);
        }
        if(parentPopulation.length() == 1) {
            childPopulation = childPopulation.appendAll(parentPopulation);
        }
        return childPopulation;
    }

    private Tuple2<Individual, Individual> drawIndividualPair() {
        return Tuple.of(getAndRemove(drawIndex()), getAndRemove(drawIndex()));
    }

    private Tuple2<Individual, Individual> interbreedPair(final Tuple2<Individual, Individual> individualPair) {
        final int drawCuttingPoint = drawCuttingPoint();
        final Individual child1 = new Individual(
                individualPair._1.getIndividual().take(drawCuttingPoint)
                        .appendAll(individualPair._2.getIndividual().takeRight(8 - drawCuttingPoint)));
        final Individual child2 = new Individual(
                individualPair._2.getIndividual().take(drawCuttingPoint)
                        .appendAll(individualPair._1.getIndividual().takeRight(8 - drawCuttingPoint)));
        return Tuple.of(child1, child2);
    }

    private Individual getAndRemove(final int index) {
        var individual = parentPopulation.get(index);
        parentPopulation = parentPopulation.removeAt(index);
        return individual;
    }

    private boolean drawIfInterbreed() {
        return random.nextFloat() <= interbreedingProbability;
    }

    private int drawCuttingPoint() {
        return random.nextInt(7) + 1;
    }

    private int drawIndex() {
        return random.nextInt(parentPopulation.size());
    }
}
