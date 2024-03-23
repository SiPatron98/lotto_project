package pl.lotto.domain.numbergenerator;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class WinningNumbersRepositoryTestImpl implements WinningNumbersRepository{

    private final Map<LocalDateTime, WinningNumbers> winningNumbersMap = new ConcurrentHashMap<>();
    @Override
    public Optional<WinningNumbers> findNumbersByDate(LocalDateTime date) {
        return Optional.ofNullable(winningNumbersMap.get(date));
    }

    @Override
    public boolean existsByDate(LocalDateTime nextDrawDate) {
        winningNumbersMap.get(nextDrawDate);
        return true;
    }

    @Override
    public WinningNumbers save(WinningNumbers winningNumbers) {
        return winningNumbersMap.put(winningNumbers.date(), winningNumbers);
    }
}
