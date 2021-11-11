package pl.com.seremak.service;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import jakarta.inject.Singleton;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.com.seremak.Util.QuadraticEquation;
import pl.com.seremak.Util.ResultFileWriter;
import pl.com.seremak.model.InputParameters;
import pl.com.seremak.model.Population;

@Slf4j
@Data
@Singleton
@RequiredArgsConstructor
public class GeneticAlgorithmService {

    public static final String RESULT_LINE_PATTERN = "f(%d) %d";
    private final MutationService mutationService;
    private final InterbreedingService interbreedingService;
    private final SelectionService selectionService;
    private Population population;
    private ResultFileWriter writer;
    private InputParameters params;

    public final List<String> run() {
        setup(params);
        var results = Stream.rangeClosed(1, params.getRunsNumber())
                .peek(i -> population.generatePopulation(params.getIndividualsNumber()))
                .map(i -> singleRun())
                .map(this::getMax)
                .map(this::createResultString)
//                .peek(log::info)
                .toList();
//        writer.writeResult(results);
//        Statistics.printStatistics(results);
        return results;
    }

    private List<Tuple2<Integer, Double>> singleRun() {
        return Stream.rangeClosed(1, params.getPopulationsNumber())
                .map(i -> createNextGeneration())
                .peek(this::setPopulation)
                .map(Population::toIntegerList)
                .map(this::toArgumentAndFunctionValueTuple)
                .map(this::getMax)
                .collect(List.collector());
    }

    private Population createNextGeneration() {
        return Stream.of(population)
                .map(interbreedingService::performInterbreedingInPopulation)
                .map(mutationService::performMutation)
                .map(selectionService::selectNewPopulation)
                .map(Population::of)
                .get();
    }

    private void setup(final InputParameters params) {
        interbreedingService.setInterbreedingProbability(params.getInterbreedingProbability());
        mutationService.setMutationProbability(params.getMutationProbability());
        selectionService.setParameters(params.getA(), params.getB(), params.getC(), params.getIndividualsNumber());
        writer = new ResultFileWriter();
        population = new Population();
    }

    private List<Tuple2<Integer, Double>> toArgumentAndFunctionValueTuple(final List<Integer> individualsValues) {
        return individualsValues
                .map(x -> Tuple.of(x, QuadraticEquation.calculateValue(x, params.getA(), params.getB(), params.getC())))
                .collect(List.collector());
    }

    private Tuple2<Integer, Double> getMax(final List<Tuple2<Integer, Double>> tupleList) {
        var newTuple = Tuple.of(0, Double.NEGATIVE_INFINITY);
        for(var tuple : tupleList) {
            if(tuple._2 > newTuple._2) newTuple = Tuple.of(tuple._1, tuple._2);
        }
        return newTuple;
    }

    private String createResultString(final Tuple2<Integer, Double> result) {
        return Stream.of(result)
                .map(tuple -> RESULT_LINE_PATTERN.formatted(tuple._1, tuple._2.intValue()))
                .get();
    }
}
