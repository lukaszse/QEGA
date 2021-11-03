package pl.com.seremak.service

import io.vavr.Tuple2
import pl.com.seremak.model.Individual
import pl.com.seremak.model.Population
import spock.lang.Specification

class InterbreedingServiceTest extends Specification {

    def 'should draw pair of individuals and remove it from list'() {

        given: 'creating new population'
        def population = new Population(5)

        and: 'creating new InterbreedingService with previously created population as a parameter'
        InterbreedingService interbreedingService = new InterbreedingService(population)

        when:
        var individualPair1 = interbreedingService.drawIndividualPair()
        var individualPair2 = interbreedingService.drawIndividualPair()


        then:
        individualPair1 instanceof Tuple2<Individual, Individual>
        individualPair1._1().getIndividual().size() == 8
        individualPair1._2().getIndividual().size() == 8
        individualPair2 instanceof Tuple2<Individual, Individual>
        individualPair2._1().getIndividual().size() == 8
        individualPair2._2().getIndividual().size() == 8
        interbreedingService.getParentPopulation().size() == 1
    }
}
