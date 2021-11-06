package pl.com.seremak.service;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.Data;
import pl.com.seremak.model.Individual;
import pl.com.seremak.model.ProbabilityInterval;

import java.util.Random;

@Singleton
public class SelectionService {

    private final Random random;

    private int a;
    private int b;
    private int c;


    SelectionService() {
        this.random = new Random();
    }

    public void setQuadraticEquationFactors(final int a, final int b, final int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double calculateFunctionValueSum(final List<Individual> population) {
        return population
                .map(Individual::toInt)
                .map(individualValue ->
                        QuadraticEquation.calculateValue(individualValue, a, b, c))
                .sum()
                .doubleValue();
    }

    private Individual drawWithRouletteWheel(final List<Individual> population) {
        return population.get(drawIndexWithRouletteWheel(calculateProbabilityIntervals(population)));
    }

    private int drawIndexWithRouletteWheel(final List<ProbabilityInterval> probabilityIntervals) {
        var drawnNumber = random.nextDouble();
        return probabilityIntervals.indexWhere(probabilityInterval ->
                probabilityInterval.getFrom() < drawnNumber && drawnNumber < probabilityInterval.getTo());
    }

    private List<ProbabilityInterval> calculateProbabilityIntervals(final List<Individual> population) {
        final List<Double> probabilityIntervalList = population
                .map(individual -> individualSelectionProbability(population, individual))
                .prepend(0d)
                .append(1d)
                .collect(List.collector());
        return Stream.range(0, probabilityIntervalList.length()-1)
                .map(i -> ProbabilityInterval.of(probabilityIntervalList.get(i), probabilityIntervalList.get(i + 1)))
                .collect(List.collector());
    }

    private double individualSelectionProbability(final List<Individual> population, final Individual individual) {
        var functionSumValue = calculateFunctionValueSum(population);
        var individualValue = QuadraticEquation.calculateValue(individual.toInt(), a, b, c);
        return functionSumValue != 0 || individualValue != 0 ?
                individualValue / calculateFunctionValueSum(population) :
                0;
    }
}
