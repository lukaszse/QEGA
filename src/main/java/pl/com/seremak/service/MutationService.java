package pl.com.seremak.service;

import io.vavr.collection.List;
import pl.com.seremak.model.Individual;
import pl.com.seremak.model.Population;

import java.util.Random;

public class MutationService {

    private final List<Individual> populationAfterInterbreeding;
    private List<Individual> mutatedPopulation;
    private final double mutationProbability;
    private final Random random;

    public MutationService(final Population populationAfterInterbreeding, final double mutationProbability) {
        this.populationAfterInterbreeding = List.ofAll(populationAfterInterbreeding.getIndividuals());
        this.mutationProbability = mutationProbability;
        this.mutatedPopulation = List.empty();
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
