package pl.com.seremak;

import io.micronaut.configuration.picocli.PicocliRunner;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Command(name = "QEGA", description = "This is application for finding minimum of quadratic function with use of genetic algorithm",
        mixinStandardHelpOptions = true)
public class QEGACommand implements Runnable {


    @Option(names = {"-r", "--runs"}, description = "Number of program runs", defaultValue = "40")
    int runsNumber;

    @Option(names = {"-p", "--populations"}, description = "Number of populations (generations)", defaultValue = "10")
    int populationsNumber;

    @Option(names = {"-i", "--individuals"}, description = "Number of individuals in population (population size)", defaultValue = "10")
    int individualsNumber;

    @Option(names = {"-b", "--interbreeding"}, description = "Interbreeding probability of individuals", defaultValue = "0.75")
    int interbreedingProbability;

    @Option(names = {"-m", "--mutation"}, description = "Mutation probability of individuals", defaultValue = "0.05")
    int mutationProbability;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(QEGACommand.class, args);
    }

    public void run() {
        // business logic here


    }
}
