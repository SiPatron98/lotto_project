package pl.lotto.domain.numberreceiver;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.AdjustableClock;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;

import java.time.*;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class NumberReceiverFacadeTest {

    private final TicketRepository ticketRepository = new TicketRepositoryTestImpl();
    Clock clock = Clock.systemUTC();



    @Test
    public void shouldReturnSuccessWhenUserGaveSixNumbers() {
        // given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();

        TicketDto generatedTicket = TicketDto.builder()
                .hash(hashGenerator.getHash())
                .numbers(numbersFromUser)
                .drawDate(nextDrawDate)
                .build();

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(generatedTicket, ValidationResult.INPUT_SUCCESS.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void shouldReturnFailedWhenUserGaveLessThanSixNumbers() {
        // given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_SIX_NUMBERS_GIVEN.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void shouldReturnFailedWhenUserGaveMoreThanSixNumbers() {
        // given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_SIX_NUMBERS_GIVEN.info);
        assertThat(response).isEqualTo(expectedResponse);

    }

    @Test
    public void shouldReturnFailedWhenUserGaveAtLeastOneNumberOutOfRangeOf1To99() {
        // given
        HashGenerator hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2000, 3, 4, 5, 6);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_IN_RANGE.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void shouldReturnCorrectHash() {
        // given
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        // when
        String response = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().hash();

        // then
        assertThat(response).hasSize(36);
        assertThat(response).isNotNull();
    }

    @Test
    public void shouldReturnCorrectDrawDate() {
        // given
        Clock clock = Clock.fixed(LocalDateTime.of(2024, 3, 22, 10, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        // when
        LocalDateTime testedDrawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();

        // then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2024, 3, 23, 12, 0, 0);
        assertThat(testedDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void shouldReturnNextSaturdayDrawDateWhenDateIsSaturdayNoon() {
        // given
        Clock clock = Clock.fixed(LocalDateTime.of(2024, 3, 23, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        // when
        LocalDateTime testedDrawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();

        // then

        LocalDateTime expectedDrawDate = LocalDateTime.of(2024, 3, 30, 12, 0, 0);
        assertThat(testedDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void shouldReturnNextSaturdayDrawDateWhenDateIsSaturdayAfternoon() {
        // given
        Clock clock = Clock.fixed(LocalDateTime.of(2024, 3, 23, 15, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        // when
        LocalDateTime testedDrawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();

        // then

        LocalDateTime expectedDrawDate = LocalDateTime.of(2024, 3, 30, 12, 0, 0);
        assertThat(testedDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void shouldReturnEmptyCollectionsIfThereAreNoTickets() {
        // given
        HashGenerable hashGenerator = new HashGenerator();
        Clock clock = Clock.fixed(LocalDateTime.of(2024, 3, 15, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        LocalDateTime drawDate = LocalDateTime.now(clock);

        // when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate);
        // then
        assertThat(allTicketsByDate).isEmpty();
    }


    @Test
    public void shouldReturnEmptyCollectionsIfGivenDateIsAfterNextDrawDate() {
        // given
        HashGenerable hashGenerator = new HashGenerator();

        Clock clock = Clock.fixed(LocalDateTime.of(2024, 3, 22, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        NumberReceiverResponseDto numberReceiverResponseDto = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));

        LocalDateTime drawDate = numberReceiverResponseDto.ticketDto().drawDate();

        // when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate.plusWeeks(1L));
        // then
        assertThat(allTicketsByDate).isEmpty();
    }

    @Test
    public void shouldReturnTicketsWithCorrectDrawDate() {
        // given
        HashGenerable hashGenerator = new HashGenerator();

        Instant fixedInstant = LocalDateTime.of(2024, 3, 21, 12, 0, 0).toInstant(ZoneOffset.UTC);
        ZoneId of = ZoneId.of("Europe/London");
        AdjustableClock clock = new AdjustableClock(fixedInstant, of);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        NumberReceiverResponseDto numberReceiverResponseDto = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto1 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto2 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto3 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        TicketDto ticketDto = numberReceiverResponseDto.ticketDto();
        TicketDto ticketDto1 = numberReceiverResponseDto1.ticketDto();
        LocalDateTime drawDate = numberReceiverResponseDto.ticketDto().drawDate();
        // when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate);
        // then
        assertThat(allTicketsByDate).containsOnly(ticketDto, ticketDto1);
    }

}