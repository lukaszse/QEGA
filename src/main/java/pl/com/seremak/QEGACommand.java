package pl.com.seremak;

import io.micronaut.configuration.picocli.PicocliRunner;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import pl.com.seremak.model.InputParameters;
import pl.com.seremak.service.GeneticAlgorithmService;

@Slf4j
@Command(name = "QEGA", description = "This is application for finding maksimum of quadratic function with use of genetic algorithm",
        mixinStandardHelpOptions = true)
public class QEGACommand implements Runnable {

    @Inject
    GeneticAlgorithmService geneticAlgorithmService;

    @Option(names = {"-a"}, description = "Factor a", defaultValue = "-1")
    int a;

    @Option(names = {"-b"}, description = "Factor b", defaultValue = "100")
    int b;

    @Option(names = {"-c"}, description = "Factor c", defaultValue = "0")
    int c;

    @Option(names = {"-r", "--runs"}, description = "Number of program runs", defaultValue = "40")
    int runsNumber;

    @Option(names = {"-p", "--populations"}, description = "Number of populations (generations)", defaultValue = "10")
    int populationsNumber;

    @Option(names = {"-i", "--individuals"}, description = "Number of individuals in population (population size)", defaultValue = "10")
    int individualsNumber;

    @Option(names = {"-t", "--interbreeding"}, description = "Interbreeding probability of individuals", defaultValue = "0.7")
    double interbreedingProbability;

    @Option(names = {"-m", "--mutation"}, description = "Mutation probability of individuals", defaultValue = "0.15")
    double mutationProbability;


    public static void main(String[] args) throws Exception {
        PicocliRunner.run(QEGACommand.class, args);
    }

    public void run() {

        log.info("QEGA application started");
        log.info("Input parameters: number of runs = {}, number of populations = {}, number of individuals = {}", runsNumber, populationsNumber, individualsNumber);
        log.info("interbreeding probability = {}, mutation probability {}", interbreedingProbability, mutationProbability);

        log.info("Looking for maximum of function: %d*x^2 + %d*x + %d".formatted(a, b, c));

        validateInput(populationsNumber, individualsNumber);
        geneticAlgorithmService.setParams(
                InputParameters.builder()
                        .a(a)
                        .b(b)
                        .c(c)
                        .runsNumber(runsNumber)
                        .populationsNumber(populationsNumber)
                        .individualsNumber(individualsNumber)
                        .interbreedingProbability(interbreedingProbability)
                        .mutationProbability(mutationProbability)
                        .build());

        geneticAlgorithmService.run();
    }

    private static void validateInput(final int populationsNumber, final int individualsNumber) {
        if (populationsNumber * individualsNumber > 150) {
            throw new IllegalArgumentException("populationsNumber * individualsNumber cannot exceed 150");
        }
    }
}
