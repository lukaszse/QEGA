package pl.com.seremak.service

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.vavr.collection.List
import jakarta.inject.Inject
import pl.com.seremak.model.Individual
import pl.com.seremak.model.InputParameters
import spock.lang.Specification

@MicronautTest
class SelectionServiceTest extends Specification {

    @Inject
    SelectionService selectionService

    def 'should calculate sum of function values for given population'() {

        given: 'prepare individuals'
        def population = preparePopulation()

        and: 'set input quadratic function factors'
        selectionService.setQuadraticEquationFactors(a, b, c)

        when: 'calculate sum'
        def sum = selectionService.calculateFunctionValueSum(population)

        then:
        sum == expectedResult

        where:
        a | b | c | expectedResult
        2 | 2 | 2 | 9160
        3 | 1 | 0 | 13582
        2 | 0 | 0 | 9006
        0 | 0 | 0 | 0
    }

    def 'should calculate probability of individual selection'() {

        given: 'prepare individuals'
        def population = preparePopulation()

        and: 'set input quadratic function factors'
        selectionService.setQuadraticEquationFactors(a, b, c)

        when: 'calculate individual selection probability'
        def probability = selectionService.individualSelectionProbability(population, population.get(1))

        then:
        probability.round(4) == expectedProbability

        where:
        a | b | c | expectedProbability
        2 | 2 | 2 | 0.0015
        3 | 1 | 0 | 0.0010
        2 | 0 | 0 | 0.0009
        0 | 0 | 0 | 0
    }


    static def preparePopulation() {
        def individual1 = Individual.of(List.of(false, false, false, false, false, false, false, true))
        def individual2 = Individual.of(List.of(false, false, false, false, false, false, true, false))
        def individual3 = Individual.of(List.of(false, false, false, false, false, false, true, true))
        def individual4 = Individual.of(List.of(false, true, false, false, false, false, true, true))
        return List.of(individual1, individual2, individual3, individual4)
    }
}
