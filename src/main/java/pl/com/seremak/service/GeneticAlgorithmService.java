package pl.com.seremak.service;

import io.micronaut.runtime.context.scope.ThreadLocal;
import io.vavr.collection.Stream;
import jakarta.inject.Singleton;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.com.seremak.model.InputParameters;
import pl.com.seremak.model.Population;

@Data
@Singleton
@RequiredArgsConstructor
public class GeneticAlgorithmService {


    private final MutationService mutationService;
    private final InterbreedingService interbreedingService;
    private final Population population;
    private InputParameters inputParameters;


    public final void run() {
        setInputParameters(inputParameters);
        population.generatePopulation(inputParameters.getIndividualsNumber());

    }

    private void setParameters(final InputParameters inputParameters) {
        population.generatePopulation(inputParameters.getIndividualsNumber());
        interbreedingService.setInterbreedingProbability(inputParameters.getInterbreedingProbability());
        mutationService.setMutationProbability(inputParameters.getMutationProbability());
    }

    private Population createNextGeneration() {
        var children = interbreedingService.performInterbreedingInPopulation(population);
        var mutatedChildren = mutationService.performMutation(children);
        return null;
    }
}
