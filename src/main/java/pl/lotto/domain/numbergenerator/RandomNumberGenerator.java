package pl.lotto.domain.numbergenerator;

import pl.lotto.domain.numbergenerator.dto.OneRandomNumberResponseDto;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class RandomNumberGenerator implements  RandomNumberGenerable{

    private static final int LOWER_BAND = 1;
    private static final int UPPER_BAND = 99;

    private final OneRandomNumberFetcher client;

    RandomNumberGenerator(OneRandomNumberFetcher client) {
        this.client = client;
    }

    @Override
    public Set<Integer> generateSixRandomNumbers() {
        Set<Integer> winningNumbers = new HashSet<>();
        while (isAmountOfNumbersLowerThanSix(winningNumbers)) {
            OneRandomNumberResponseDto oneRandomNumberResponseDto = client.retrieveOneRandomNumber(LOWER_BAND, UPPER_BAND);
            int randomNumber = oneRandomNumberResponseDto.number();
            winningNumbers.add(randomNumber);
        }
        return winningNumbers;
    }

    private boolean isAmountOfNumbersLowerThanSix(Set<Integer> winningNumbers) {
        return winningNumbers.size() < 6;
    }
}
