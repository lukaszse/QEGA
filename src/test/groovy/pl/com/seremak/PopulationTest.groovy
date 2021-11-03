package pl.com.seremak

import spock.lang.Specification

class PopulationTest extends Specification {

    def 'should generate population with correct number of individuals'() {

        when:
        var population = new Population(individualsNumber as int)

        then:
        population.getIndividuals().size() == individualsNumber

        where:
        individualsNumber << [5, 8, 3, 40, 17, 92, 83]
    }
}
