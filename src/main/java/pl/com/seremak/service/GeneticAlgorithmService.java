package pl.com.seremak.service;

import io.micronaut.runtime.context.scope.ThreadLocal;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.com.seremak.model.InputParameters;
import pl.com.seremak.model.Population;

@Data
@ThreadLocal
@RequiredArgsConstructor
public class GeneticAlgorithmService {


    private final MutationService mutationService;
    private final InterbreedingService interbreedingService;
    private final Population population;
    private InputParameters inputParameters;


    final void run() {
        population.generatePopulation(inputParameters.getIndividualsNumber());
        interbreedingService.setInterbreedingProbability(inputParameters.getInterbreedingProbability());

    }
}
