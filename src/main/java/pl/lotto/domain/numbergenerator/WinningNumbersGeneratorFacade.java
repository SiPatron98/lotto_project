package pl.lotto.domain.numbergenerator;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numbergenerator.dto.SixRandomNumbersDto;
import pl.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
public class WinningNumbersGeneratorFacade {

    private final RandomNumberGenerable randomGenerable;
    private final WinningNumberValidator winningNumberValidator;
    private final WinningNumbersRepository winningNumbersRepository;
    private final NumberReceiverFacade numberReceiverFacade;
    private final WinningNumbersGeneratorFacadeConfigurationProperties properties;

    public WinningNumbersDto generateWinningNumbers() {
        LocalDateTime nextDrawDate = numberReceiverFacade.retrieveNextDrawDate();
        SixRandomNumbersDto dto = randomGenerable.generateSixRandomNumbers(properties.count(), properties.lowerBand(), properties.upperBand());
        Set<Integer> winningNumbers = dto.numbers();
        winningNumberValidator.validate(winningNumbers);
        winningNumbersRepository.save(WinningNumbers.builder()
                .winningNumbers(winningNumbers)
                .date(nextDrawDate)
                .build());
        return WinningNumbersDto.builder()
                .winningNumbers(winningNumbers)
                .build();
    }

    public WinningNumbersDto retrieveWinningNumbersByDate(LocalDateTime date) {
        WinningNumbers numbersByDate = winningNumbersRepository.findNumbersByDate(date)
                .orElseThrow(() -> new WinningNumbersNotFoundException("Not found"));

        return WinningNumbersDto.builder()
                .winningNumbers(numbersByDate.winningNumbers())
                .date(numbersByDate.date())
                .build();
    }

    public boolean areWinningNumbersGeneratedByDate() {
        LocalDateTime nextDrawDate = numberReceiverFacade.retrieveNextDrawDate();
        return winningNumbersRepository.existsByDate(nextDrawDate);
    }
}
