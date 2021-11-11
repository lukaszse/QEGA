package pl.com.seremak.model

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import pl.com.seremak.model.Population
import spock.lang.Specification

@MicronautTest
class PopulationTest extends Specification {

    Population population = new Population();

    def 'should generate population with correct number of individuals'() {

        when: 'generate population'
        population.generatePopulation(individualsNumber as int)

        then:
        population.getIndividuals().size() == individualsNumber

        where:
        individualsNumber << [5, 8, 3, 40, 17, 92, 83]
    }
}
