package pl.com.seremak.tuner

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.vavr.collection.Stream
import jakarta.inject.Inject
import pl.com.seremak.Util.Statistics
import pl.com.seremak.model.InputParameters
import pl.com.seremak.service.GeneticAlgorithmService
import spock.lang.Specification

@MicronautTest
class AlgorithmTuner extends Specification {

    @Inject
    GeneticAlgorithmService geneticAlgorithmService;

    def 'tune algorithm'() {

        given:
        geneticAlgorithmService.setParams(InputParameters.builder()
                .a(-1)
                .b(0)
                .c(100)
                .interbreedingProbability(interbreedingProbability)
                .mutationProbability(mutationProbability)
                .populationsNumber(populationsNumber)
                .individualsNumber(individualsNumber)
                .runsNumber(40)
                .build())


        when:
        def sdList = Stream.rangeClosed(1, 10)
                .map(i -> geneticAlgorithmService.run())
                .map(result -> Statistics.parseFunctionValuesList(result))
                .map(functionValueList -> Statistics.sd(functionValueList))
                .toList()

        and:
        def meanSd = sdList.sum() / sdList.length()
        def sdOfSd = Statistics.sd(sdList)

        then:
        println("runs number: " + sdList.length())
        println("list of calculated standard deviations: " + sdList)
        println("mean sd: " + meanSd)
        println("sd of sd: " + sdOfSd)


        where:
        interbreedingProbability | mutationProbability | individualsNumber | populationsNumber
        0.8                      | 0.08                 | 6                | 25

    }
}
