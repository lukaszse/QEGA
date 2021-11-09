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

    private final MutationService mutationService;
    private final InterbreedingService interbreedingService;
    private final SelectionService selectionService;
    private final Population population;
    private ResultFileWriter writer;
    private InputParameters params;
    private List<String> results;

    public final void run() {
        setParameters(params);
        writer = new ResultFileWriter();
        population.generatePopulation(params.getIndividualsNumber());

        var results = Stream.rangeClosed(1, params.getRunsNumber())
                .map(i -> singleRun())
                .map(this::createResultString)
                .peek(log::info)
                .toList();

        writer.writeResult(results);
    }

    private Tuple2<Integer, Double> singleRun() {
        var list = Stream.rangeClosed(1, params.getPopulationsNumber())
                .map(i -> createNextGeneration())
                .map(Population::toIntegerList)
                .map(this::toTupleList)
                .map(this::getMax)
                .collect(List.collector());
        return getMax(list);
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

    private List<Tuple2<Integer, Double>> toTupleList(final List<Integer> individualsValues) {
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
                .map(tuple -> "f(%d) %d".formatted(tuple._1, tuple._2.intValue()))
                .get();
    }
}
