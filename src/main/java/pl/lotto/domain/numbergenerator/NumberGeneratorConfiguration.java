package pl.lotto.domain.numbergenerator;

import pl.lotto.domain.numberreceiver.*;

public class NumberGeneratorConfiguration {
    WinningNumbersGeneratorFacade createForTest(
            RandomNumberGenerable generator,
            WinningNumbersRepository winningNumberRepository,
            NumberReceiverFacade numberReceiverFacade
    ) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        return new WinningNumbersGeneratorFacade(generator, winningNumberValidator, winningNumberRepository, numberReceiverFacade);
    }
}
