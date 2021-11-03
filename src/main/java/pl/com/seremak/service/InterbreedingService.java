package pl.com.seremak.service;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.Data;
import pl.com.seremak.model.Individual;
import pl.com.seremak.model.Population;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Data
public class InterbreedingService {

    private List<Individual> parentPopulation;
    private List<Individual> childPopulation;
    private final Random random;

    InterbreedingService(Population parentPopulation) {
        this.parentPopulation = new LinkedList<>(parentPopulation.getIndividuals());
        this.random = new Random();
    }

    public Tuple2<Individual, Individual> drawIndividualPair() {
        return Tuple.of(getAndRemove(drawIndex()), getAndRemove(drawIndex()));
    }

    private Individual getAndRemove(final int index) {
        var individual = parentPopulation.get(index);
        parentPopulation.remove(index);
        return individual;
    }

    private int drawCuttingPoint() {
        return draw(7) + 1;
    }

    private int drawIndex() {
        return draw(parentPopulation.size());
    }

    private int draw(final int size) {
        return random.nextInt(size);
    }
}
