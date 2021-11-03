package pl.com.seremak

import spock.lang.Specification

class IndividualTest extends Specification {

    def 'should generate Individual'() {

        when: 'make new individual'
        def individual = new Individual()

        then: 'should create individual which contains list of 8 booleans'
        individual.getIndividual().size() == 8
        println individual
    }

    def 'should convert binary coded individual to int correctly'() {

        given:
        def individual = new Individual()
        individual.setIndividual(givenIndividual)

        when:
        def result = individual.toInt()

        then:
        result == intValue

        where:
        intValue | givenIndividual
        0        | List.of(false, false, false, false, false, false, false, false)
        1        | List.of(false, false, false, false, false, false, false, true)
        9        | List.of(false, false, false, false, true, false, false, true)
        65       | List.of(false, true, false, false, false, false, false, true)
        24       | List.of(false, false, false, true, true, false, false, false)
        254      | List.of(true, true, true, true, true, true, true, false)
        255      | List.of(true, true, true, true, true, true, true, true)
    }
}
