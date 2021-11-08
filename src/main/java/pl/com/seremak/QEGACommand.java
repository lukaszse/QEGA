package pl.com.seremak;

import io.micronaut.configuration.picocli.PicocliRunner;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import pl.com.seremak.model.InputParameters;
import pl.com.seremak.service.GeneticAlgorithmService;

@Slf4j
@Command(name = "QEGA", description = "This is application for finding minimum of quadratic function with use of genetic algorithm",
        mixinStandardHelpOptions = true)
public class QEGACommand implements Runnable {

    @Inject
    GeneticAlgorithmService geneticAlgorithmService;

    @Option(names = {"-a"}, description = "Factor a", defaultValue = "-1")
    int a;

    @Option(names = {"-b"}, description = "Factor b", defaultValue = "0")
    int b;

    @Option(names = {"-c"}, description = "Factor c", defaultValue = "100")
    int c;

    @Option(names = {"-r", "--runs"}, description = "Number of program runs", defaultValue = "40")
    int runsNumber;

    @Option(names = {"-p", "--populations"}, description = "Number of populations (generations)", defaultValue = "10")
    int populationsNumber;

    @Option(names = {"-i", "--individuals"}, description = "Number of individuals in population (population size)", defaultValue = "10")
    int individualsNumber;

    @Option(names = {"-t", "--interbreeding"}, description = "Interbreeding probability of individuals", defaultValue = "0.75")
    double interbreedingProbability;

    @Option(names = {"-m", "--mutation"}, description = "Mutation probability of individuals", defaultValue = "0.05")
    double mutationProbability;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(QEGACommand.class, args);
    }

    public void run() {

        log.info("QEGA application started");

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
        if (populationsNumber * individualsNumber >= 150) {
            throw new IllegalArgumentException("populationsNumber * individualsNumber cannot exceed 150");
        }
    }
}
