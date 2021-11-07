package pl.com.seremak.service;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.collection.Traversable;
import io.vavr.control.Option;
import jakarta.inject.Singleton;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.com.seremak.Util.ResultFileWriter;
import pl.com.seremak.model.InputParameters;
import pl.com.seremak.model.Population;
import pl.com.seremak.Util.QuadraticEquation;

@Data
@Singleton
@RequiredArgsConstructor
public class GeneticAlgorithmService {


    private final MutationService mutationService;
    private final InterbreedingService interbreedingService;
    private final SelectionService selectionService;
    private final Population population;
    private ResultFileWriter writer;
    private InputParameters params;
    private List<String> results;

    public final void run() {
        setParams(params);
        population.generatePopulation(params.getIndividualsNumber());
    }

    private String singleRun() {
        return Stream.rangeClosed(0, params.getPopulationsNumber())
                .map(i -> createNextGeneration())
                .map(Population::toIntegerList)
                .map(Traversable::max)
                .map(Option::get)
                .map(this::createResultString)
                .get();
    }

    private Population createNextGeneration() {
        return Stream.of(population)
                .map(interbreedingService::performInterbreedingInPopulation)
                .map(mutationService::performMutation)
                .map(selectionService::selectNewPopulation)
                .map(Population::of)
                .get();
    }

    private void setParameters(final InputParameters params) {
        interbreedingService.setInterbreedingProbability(params.getInterbreedingProbability());
        mutationService.setMutationProbability(params.getMutationProbability());
        selectionService.setParameters(params.getA(), params.getB(), params.getC(), params.getIndividualsNumber());
    }

    private String createResultString(final int x) {
        return Stream.of(QuadraticEquation.calculateValue(x, params.getA(), params.getB(), params.getC()))
                .map(y -> "f(%d) %f".formatted(x, y))
                .get();
    }
}
