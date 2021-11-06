package pl.com.seremak.service;

import io.vavr.collection.List;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.Data;
import pl.com.seremak.model.Individual;

@Singleton
public class SelectionService {

    private int a;
    private int b;
    private int c;

    public void setQuadraticEquationFactors(final int a, final  int b, final int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double calculateFunctionValueSum(List<Individual> population) {
        return population
                .map(Individual::toInt)
                .map(individualValue ->
                        QuadraticEquation.calculateValue(individualValue, a, b, c))
                .sum()
                .doubleValue();
    }
}
