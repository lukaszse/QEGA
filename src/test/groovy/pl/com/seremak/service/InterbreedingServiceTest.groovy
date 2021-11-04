package pl.com.seremak.service

import io.vavr.Tuple2
import io.vavr.collection.Stream
import pl.com.seremak.model.Individual
import pl.com.seremak.model.Population
import spock.lang.Specification

class InterbreedingServiceTest extends Specification {

    def 'should draw pair of individuals and remove it from list'() {

        given: 'creating new population'
        def population = new Population(individualsNumber)

        and: 'setting interbreedingProbability'
        def interbreedingProbability = 0.8

        and: 'creating new InterbreedingService with previously created population as a parameter'
        InterbreedingService interbreedingService = new InterbreedingService(population, interbreedingProbability)

        when:
        Stream.range(0, drawsNumber)
                .forEach(i -> interbreedingService.drawIndividualPair())

        then:
        interbreedingService.parentPopulation.length() == expectedPopulationSize

        where:
        individualsNumber | drawsNumber | expectedPopulationSize
        5                 | 2           | 1
        8                 | 3           | 2
        3                 | 1           | 1
        2                 | 1           | 0

    }

    def 'should interbreed pair of individuals correctly'() {

        given: 'creating new population'
        def population = new Population(numberOfIndividuals)

        and: 'creating new InterbreedingService with previously created population as a parameter'
        InterbreedingService interbreedingService = new InterbreedingService(population, interbreedingProbability)

        when:
        var children = interbreedingService.performInterbreedingInPopulation()

        then:
        children.get(0).getIndividual().length() == 8
        children.length() == numberOfIndividuals

        where:
        numberOfIndividuals | interbreedingProbability
        1                   | 0.8
        2                   | 0.9
        3                   | 0.55
        12                  | 0.66
        8                   | 0.5
        15                  | 0.33
        18                  | 0.55

    }
}
