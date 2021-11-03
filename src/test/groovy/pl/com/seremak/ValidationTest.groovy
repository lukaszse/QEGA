package pl.com.seremak

import spock.lang.Specification


class ValidationTest extends Specification {

    def 'should validate input correctly'() {

        when:
        QEGACommand.validateInput(populationsNumber, individualsNumber)

        then:
        def error = thrown(IllegalArgumentException.class)
        error.getMessage().contains("cannot exceed 150")

        where:
        populationsNumber | individualsNumber
        11                | 15
        10                | 16
        2                 | 80
        80                | 3
    }
}
