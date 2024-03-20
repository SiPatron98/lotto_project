package pl.lotto.domain.numberreceiver;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class NumberReceiverFacadeTest {

    @Test
    public void shouldReturnSuccessWhenUserGaveSixNumbers() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade();

        // when
        String result = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));

        // then
        assertThat(result).isEqualTo("success");
    }

    @Test
    public void shouldReturnFailedWhenUserGaveLessThanSixNumbers() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade();

        // when
        String result = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5));

        // then
        assertThat(result).isEqualTo("failed");
    }
    @Test
    public void shouldReturnFailedWhenUserGaveMoreThanSixNumbers() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade();

        // when
        String result = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6, 7));

        // then
        assertThat(result).isEqualTo("failed");

    }
    @Test
    public void shouldReturnFailedWhenUserGaveAtLeastOneNumberOutOfRangeOf1To99() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade();

        // when
        String result = numberReceiverFacade.inputNumbers(Set.of(1, 2000, 3, 4, 5, 6));

        // then
        assertThat(result).isEqualTo("failed");

    }

}