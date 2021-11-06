package pl.com.seremak.service;

import io.vavr.collection.List;
import jakarta.inject.Singleton;
import pl.com.seremak.model.Individual;

import java.util.Random;

@Singleton
public class MutationService {

    private List<Individual> populationAfterInterbreeding;
    private List<Individual> mutatedPopulation;
    private final double mutationProbability;
    private final Random random;

    public MutationService(final double mutationProbability) {
        this.mutationProbability = mutationProbability;
        this.random = new Random();
    }

    public List<Individual> performMutation() {
        mutatedPopulation = populationAfterInterbreeding
                .map(this::mutateIndividual);
        return mutatedPopulation;
    }

    private Individual mutateIndividual(final Individual individual) {
        return new Individual(individual.getIndividual()
                .map(gene -> drawToMutation() != gene));
    }

    private boolean drawToMutation() {
        return random.nextFloat() <= mutationProbability;
    }
}
