package pl.com.seremak.service

import pl.com.seremak.model.Population
import spock.lang.Specification

class MutationServiceTest extends Specification {

    def 'should mutate population properly' () {

        given: 'creating new population'
        def populationAfterInterBreeding = new Population(numberOfIndividuals)


        and: 'creating new mutationService'
        def mutationService = new MutationService(populationAfterInterBreeding, mutationProbability)

        when: 'perform mutation in population'
        def populationAfterMutation = mutationService.performMutation()

        then: 'should mutate objects properly'
        populationAfterMutation.length() == numberOfIndividuals
        populationAfterMutation.get(0).getIndividual().length() == 8

        where:
        numberOfIndividuals | mutationProbability
        1                   | 0.1
        2                   | 0.2
        3                   | 0.15
        12                  | 0.16
        8                   | 0.3
        15                  | 0.33
        18                  | 0.03

    }
}
