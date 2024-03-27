package pl.lotto.feature;

import org.junit.jupiter.api.Test;
import pl.lotto.BaseIntegrationTest;

public class UserPlayedLottoAndWonIntegrationTest extends BaseIntegrationTest {

    @Test
    public void shouldUserWinAndSystemShouldGenerateWinners() {
//    step 1: external service returns 6 random numbers (1,2,3,4,5,6)
//    step 2: user made POST /inputNumbers with 6 numbers (1, 2, 3, 4, 5, 6) at 20-3-2024 10:00 and system returned OK(200) with message: “success” and Ticket (DrawDate:23.3.2022 12:00 (Saturday), TicketId: sampleTicketId)
//    step 3: system fetched winning numbers for draw date: 23.3.2024 12:00
//    step 4: 3 days, 2 hours and 1 minute passed, and it is 1 minute after the draw date (23.3.2024 12:01)
//    step 5: system generated result for TicketId: sampleTicketId with draw date 23.3.2024 12:00, and saved it with 6 hits
//    step 6: 3 hours passed, and it is 1 minute after announcement time (23.3.2024 15:01)
//    step 7: user made GET /results/sampleTicketId and system returned 200 (OK)
    }
}
