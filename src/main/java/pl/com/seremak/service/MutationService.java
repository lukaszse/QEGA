package pl.com.seremak.service;

import io.vavr.collection.List;
import jakarta.inject.Singleton;
import lombok.Data;
import pl.com.seremak.model.Individual;
import pl.com.seremak.model.Population;

import java.util.Random;

@Data
@Singleton
public class MutationService {

    private final Random random;
    private double mutationProbability;

    public MutationService() {
        this.random = new Random();
    }

    public List<Individual> performMutation(final Population population) {
        return population
                .getIndividuals()
                .map(this::mutateIndividual);
    }

    private Individual mutateIndividual(final Individual individual) {
        return new Individual(individual.getIndividual()
                .map(gene -> drawToMutation() != gene));
    }

    private boolean drawToMutation() {
        return random.nextFloat() <= mutationProbability;
    }
}
