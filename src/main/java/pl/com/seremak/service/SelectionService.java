package pl.com.seremak.service;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.collection.Traversable;
import jakarta.inject.Singleton;
import pl.com.seremak.model.Individual;
import pl.com.seremak.model.Population;
import pl.com.seremak.model.ProbabilityInterval;
import pl.com.seremak.Util.QuadraticEquation;

import java.util.Random;

@Singleton
public class SelectionService {

    private final Random random;

    private int a;
    private int b;
    private int c;
    private int individualsNumber;


    SelectionService() {
        this.random = new Random();
    }

    public void setParameters(final int a, final int b, final int c, final int individualsNumber) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.individualsNumber = individualsNumber;
    }

    public List<Individual> selectNewPopulation(final List<Individual> population) {
        var probabilityIntervals = calculateProbabilityIntervals(population);
        return Stream.range(0, individualsNumber)
                .map(i -> drawWithRouletteWheel(population, probabilityIntervals))
                .collect(List.collector());
    }

    private Individual drawWithRouletteWheel(final List<Individual> population, List<ProbabilityInterval> probabilityIntervals) {
        return Stream.of(probabilityIntervals)
                .map(theProbabilityIntervals -> drawIndexWithRouletteWheel(probabilityIntervals))
                .map(population::get)
                .get();
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
                .collect(List.collector());
        return Stream.range(0, probabilityIntervalList.length() - 1)
                .map(i -> mapToProbabilityInterval(probabilityIntervalList, i))
                .collect(List.collector());
    }

    private ProbabilityInterval mapToProbabilityInterval(final List<Double> probabilityList, final int index) {
        var cumulativeValueFrom = calculateCumulativeValue(probabilityList, index);
        var cumulativeValueTo = calculateCumulativeValue(probabilityList, index + 1);
        return ProbabilityInterval.of(cumulativeValueFrom, cumulativeValueTo);
    }

    private Double calculateCumulativeValue(final List<Double> probabilityList, final int index) {
        return Stream.rangeClosed(0, index)
                .map(probabilityList::get)
                .sum()
                .doubleValue();
    }

    private double individualSelectionProbability(final List<Individual> population, final Individual individual) {
        var offset = calculateOffset(population);
        var functionSumValue = calculateFunctionValueSum(population, offset);
        var individualValue = QuadraticEquation.calculateValue(individual.toInt(), a, b, c) + offset;
        return functionSumValue != 0 || individualValue != 0 ?
                individualValue / functionSumValue :
                0;
    }

    private double calculateFunctionValueSum(final List<Individual> population, final double offset) {
        return population
                .map(Individual::toInt)
                .map(x -> QuadraticEquation.calculateValue(x, a, b, c))
                .map(f_x -> f_x + offset)
                .sum()
                .doubleValue();
    }

    private double calculateOffset(final List<Individual> population) {
        return population
                .map(Individual::toInt)
                .map(x -> QuadraticEquation.calculateValue(x, a, b, c))
                .min()
                .map(lowestIndividual -> lowestIndividual < 0 ? Math.abs(lowestIndividual) : 0)
                .get();
    }
}
