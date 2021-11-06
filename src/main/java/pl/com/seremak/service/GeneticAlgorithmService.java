package pl.com.seremak.service;

import io.vavr.collection.List;
import jakarta.inject.Singleton;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.com.seremak.model.Individual;
import pl.com.seremak.model.InputParameters;
import pl.com.seremak.model.Population;

@Data
@Singleton
@RequiredArgsConstructor
public class GeneticAlgorithmService {


    private final MutationService mutationService;
    private final InterbreedingService interbreedingService;
    private final SelectionService selectionService;
    private final Population population;
    private InputParameters param;


    public final void run() {
        setParam(param);
        population.generatePopulation(param.getIndividualsNumber());

    }

    private Population createNextGeneration() {
        var children = interbreedingService.performInterbreedingInPopulation(population);
        var mutatedChildren = mutationService.performMutation(children);
        return null;
    }

    private void setParameters(final InputParameters inputParameters) {
        population.generatePopulation(inputParameters.getIndividualsNumber());
        interbreedingService.setInterbreedingProbability(inputParameters.getInterbreedingProbability());
        mutationService.setMutationProbability(inputParameters.getMutationProbability());
        selectionService.setQuadraticEquationFactors(inputParameters.getA(), inputParameters.getB(), inputParameters.getC());
    }
}
