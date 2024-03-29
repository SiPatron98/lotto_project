package pl.lotto.domain.numbergenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.numberreceiver.*;

@Configuration
public class NumberGeneratorConfiguration {

    @Bean
    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade(
            WinningNumbersRepository winningNumbersRepository,
            NumberReceiverFacade numberReceiverFacade,
            RandomNumberGenerable randomNumberGenerator,
            WinningNumbersGeneratorFacadeConfigurationProperties properties
    ) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        return new WinningNumbersGeneratorFacade(randomNumberGenerator, winningNumberValidator, winningNumbersRepository, numberReceiverFacade, properties);
    }
    WinningNumbersGeneratorFacade createForTest(
            RandomNumberGenerable generator,
            WinningNumbersRepository winningNumberRepository,
            NumberReceiverFacade numberReceiverFacade
    ) {
        WinningNumbersGeneratorFacadeConfigurationProperties properties = WinningNumbersGeneratorFacadeConfigurationProperties.builder()
                .count(6)
                .lowerBand(1)
                .upperBand(99)
                .build();
        return winningNumbersGeneratorFacade(winningNumberRepository, numberReceiverFacade, generator, properties);
    }
}
