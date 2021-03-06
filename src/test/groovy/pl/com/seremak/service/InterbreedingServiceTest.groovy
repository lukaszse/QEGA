package pl.com.seremak.service

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.vavr.collection.Stream
import jakarta.inject.Inject
import pl.com.seremak.model.Population
import spock.lang.Specification

@MicronautTest
class InterbreedingServiceTest extends Specification {

    @Inject
    InterbreedingService interbreedingService

    def 'should draw pair of individuals and remove it from list'() {

        given: 'create new population'
        def population = new Population()
        population.generatePopulation(individualsNumber)

        and: 'define interbreedingProbability'
        interbreedingService.setInterbreedingProbability(0.8)

        and: 'pass generated population to interbreeding service'
        interbreedingService.setParentPopulation(population.getIndividuals())

        when: 'draw individuals pair from population several times'
        def children = Stream.range(0, drawsNumber)
                .map(i -> interbreedingService.drawIndividualPair())
                .flatMap(tuple -> List.of(tuple._1(), tuple._2()))
                .collect(list -> List.collect())

        then:
        children.size() == expecteChildenSize
        interbreedingService.getParentPopulation().size() == expectedParentSize

        where:
        individualsNumber | drawsNumber | expectedParentSize | expecteChildenSize
        5                 | 2           | 1                  | 4
        8                 | 3           | 2                  | 6
        3                 | 1           | 1                  | 2
        2                 | 1           | 0                  | 2
    }

    def 'should interbreed pair of individuals correctly'() {

        given: 'creating new population'
        def population = new Population()
        population.generatePopulation(individualsNumber)

        and: 'define interbreeding probability'
        interbreedingService.setInterbreedingProbability(0.8)

        when: 'pass population to interbreeding service'
        var children = interbreedingService.performInterbreedingInPopulation(population)

        then:
        children.get(0).getIndividual().length() == 8
        children.length() == individualsNumber

        where:
        individualsNumber | interbreedingProbability
        1                   | 0.8
        2                   | 0.9
        3                   | 0.55
        12                  | 0.66
        8                   | 0.5
        15                  | 0.33
        18                  | 0.55

    }
}
