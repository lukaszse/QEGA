package pl.com.seremak.service;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.collection.Traversable;
import io.vavr.control.Option;
import jakarta.inject.Singleton;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.com.seremak.Util.ResultFileWriter;
import pl.com.seremak.model.InputParameters;
import pl.com.seremak.model.Population;
import pl.com.seremak.Util.QuadraticEquation;

import java.io.FileWriter;

@Slf4j
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
        setParameters(params);
        population.generatePopulation(params.getIndividualsNumber());
        writer = new ResultFileWriter();

        var results = Stream.rangeClosed(0, params.getRunsNumber())
                .map(i -> singleRun())
                .map(this::createResultString)
                .peek(log::info)
                .toList();

        writer.writeResult(results);
    }

    private int singleRun() {
        return Stream.rangeClosed(0, params.getPopulationsNumber())
                .map(i -> createNextGeneration())
                .map(Population::toIntegerList)
                .map(Traversable::max)
                .map(Option::get)
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
                .map(Double::intValue)
                .map(y -> "f(%d) %d".formatted(x, y))
                .get();
    }
}
