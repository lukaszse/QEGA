package pl.com.seremak.service

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import pl.com.seremak.model.Population
import spock.lang.Specification

@MicronautTest
class MutationServiceTest extends Specification {

    @Inject
    Population population

    @Inject
    MutationService mutationService


    def 'should mutate population properly' () {

        given: 'creating new population'
        population.generatePopulation(individualsNumber)

        and: 'define mutation probability'
        mutationService.setMutationProbability(0.2)

        when: 'perform mutation of given population'
        def mutatedIndividuals = mutationService.performMutation(population)

        then: 'should mutate objects properly'
        mutatedIndividuals.length() == individualsNumber
        mutatedIndividuals.get(0).getIndividual().length() == 8

        where:
        individualsNumber | mutationProbability
        1                   | 0.1
        2                   | 0.2
        3                   | 0.15
        12                  | 0.16
        8                   | 0.3
        15                  | 0.33
        18                  | 0.03

    }
}
