package pl.lotto.domain.numberreceiver;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.numberreceiver.dto.InputNumbersResultDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class NumberReceiverFacadeTest {

    NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(
            new NumberValidator(),
            new InMemoryNumberReceiverRepositoryTestImpl()
    );

    @Test
    public void shouldReturnSuccessWhenUserGaveSixNumbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        // when
        InputNumbersResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        assertThat(result.message()).isEqualTo("success");
    }

    @Test
    public void shouldReturnFailedWhenUserGaveLessThanSixNumbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5);

        // when
        InputNumbersResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        assertThat(result.message()).isEqualTo("failed");
    }
    @Test
    public void shouldReturnFailedWhenUserGaveMoreThanSixNumbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7);

        // when
        InputNumbersResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        assertThat(result.message()).isEqualTo("failed");

    }
    @Test
    public void shouldReturnFailedWhenUserGaveAtLeastOneNumberOutOfRangeOf1To99() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2000, 3, 4, 5, 6);

        // when
        InputNumbersResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        assertThat(result.message()).isEqualTo("failed");
    }

    @Test
    public void shouldSaveToDatabaseWhenUserGaveSixNumbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        InputNumbersResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        LocalDateTime drawDate = LocalDateTime.now();

        // when
        List<TicketDto> ticketDtos = numberReceiverFacade.userNumbers(drawDate);

        // then
        assertThat(ticketDtos).contains(
                TicketDto.builder()
                        .ticketId(result.ticketId())
                        .drawDate(drawDate)
                        .numbersFromUser(result.numbersFromUser())
                        .build()
        );
    }

}