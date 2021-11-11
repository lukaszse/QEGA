package pl.com.seremak.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class ProbabilityInterval {

    private final double from;
    private final double to;
}
