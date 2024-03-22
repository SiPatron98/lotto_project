package pl.lotto.domain.numbergenerator;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WinningNumbersGeneratorFacadeTest {

    private final WinningNumbersRepository winningNumbersRepository = new WinningNumbersRepositoryTestImpl();

    NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);

    @Test
    public void shouldReturnSetOfRequiredSize() {
        //given
        RandomNumberGenerable generator = new RandomNumberGenerator();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade numbersGenerator = new NumberGeneratorConfiguration().createForTest(
                generator,
                winningNumbersRepository,
                numberReceiverFacade
        );

        //when
        WinningNumbersDto generatedNumbers = numbersGenerator.generateWinningNumbers();

        //then
        assertThat(generatedNumbers.getWinningNumbers().size()).isEqualTo(6);
    }

    @Test
    public void shouldReturnSetOfRequiredSizeWithinRequiredRange() {
        //given
        RandomNumberGenerable generator = new RandomNumberGenerator();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade numbersGenerator = new NumberGeneratorConfiguration().createForTest(
                generator,
                winningNumbersRepository,
                numberReceiverFacade
        );

        //when
        WinningNumbersDto generatedNumbers = numbersGenerator.generateWinningNumbers();

        //then
        int upperBand = 99;
        int lowerBand = 1;
        Set<Integer> winningNumbers = generatedNumbers.getWinningNumbers();
        boolean numbersInRange = winningNumbers.stream().allMatch(number -> number >= lowerBand && number <= upperBand);
        assertThat(numbersInRange).isTrue();
    }
    @Test
    public void shouldReturnCollectionOfUniqueValues() {
        //given
        RandomNumberGenerable generator = new RandomNumberGenerator();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade numbersGenerator = new NumberGeneratorConfiguration().createForTest(
                generator,
                winningNumbersRepository,
                numberReceiverFacade
        );

        //when
        WinningNumbersDto generatedNumbers = numbersGenerator.generateWinningNumbers();

        //then
        int generatedNumbersSize = new HashSet<>(generatedNumbers.getWinningNumbers()).size();
        assertThat(generatedNumbersSize).isEqualTo(6);
    }

}