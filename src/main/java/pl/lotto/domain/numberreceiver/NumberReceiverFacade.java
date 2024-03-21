package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numberreceiver.dto.InputNumbersResultDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
public class NumberReceiverFacade {

    private final NumberValidator validator;
    private NumberReceiverRepository repository;

    public InputNumbersResultDto inputNumbers(Set<Integer> numbersFromUser) {

        boolean areAllNumbersInRange = validator.filterAllNumbersOutOfRange(numbersFromUser);
        if (areAllNumbersInRange) {
            String ticketId = UUID.randomUUID().toString();
            LocalDateTime drawDate = LocalDateTime.now();
            Ticket savedTicket = repository.save(new Ticket(ticketId, drawDate, numbersFromUser));
            return InputNumbersResultDto.builder()
                    .drawDate(savedTicket.drawDate())
                    .ticketId(savedTicket.ticketId())
                    .message("success")
                    .numbersFromUser(numbersFromUser)
                    .build();
        }
        return InputNumbersResultDto.builder()
                .message("failed")
                .build();
    }

    public List<TicketDto> userNumbers(LocalDateTime date) {
        List<Ticket> allTicketsByDrawDate = repository.findAllTicketsByDrawDate(date);
        return allTicketsByDrawDate.stream()
                .map(TicketMapper::mapFromTicket)
                .toList();
    }

}
