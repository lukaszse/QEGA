package pl.com.seremak.Util;

import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;

import java.util.DoubleSummaryStatistics;

@Slf4j
public class Statistics {

    public static void printStatistics(final List<String> results) {
        var functionValuesList = parseFunctionValuesList(results);
        log.info("ResultsNumber={}, Mean={}, Median={}, StandardDeviation={}", functionValuesList.length(), mean(functionValuesList), median(functionValuesList), sd(functionValuesList));
    }

    public static List<Double> parseFunctionValuesList(final List<String> results) {
        return results
                .map(resultString -> List.of(resultString.split(" ")))
                .map(List::last)
                .map(Double::parseDouble)
                .collect(List.collector());
    }

    public static int sum(List<Double> a) {
        if (a.size() > 0) {
            int sum = 0;
            for (Double i : a) {
                sum += i;
            }
            return sum;
        }
        return 0;
    }

    public static double mean(List<Double> a) {
        int sum = sum(a);
        double mean = 0;
        mean = sum / (a.size() * 1.0);
        return mean;
    }

    public static double median(List<Double> a) {
        int middle = a.size() / 2;
        if (a.size() % 2 == 1) {
            return a.get(middle);
        } else {
            return (a.get(middle - 1) + a.get(middle)) / 2.0;
        }
    }

    public static double sd(List<Double> a) {
        double sum = 0;
        double mean = mean(a);
        for (Double i : a)
            sum += Math.pow((i - mean), 2);
        return Math.sqrt(sum / (a.size() - 1));
    }
}
