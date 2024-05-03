package ru.mts.petprojectservices.scheduler;

import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.mts.petprojectservices.entity.Favor;
import ru.mts.petprojectservices.repository.FavorRepository;
import ru.mts.petprojectservices.service.ClientService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavorScheduler {

    @Value("${app.time-after-request-to-work.minutes:5}")
    private int minutes;
    @Value("${app.time-after-request-to-work.hours:0}")
    private int hours;

    private final FavorRepository favorRepository;
    private final ClientService clientService;

    @Async
    @Scheduled(fixedRateString = "${app.scheduling.check-interval}")
    @Counted(value = "scheduled", extraTags = {"method", "automaticallyRequestToWork"})
    public void automaticallyRequestToWork() {
        log.info("Scheduled requestToWork run");
        favorRepository.findAll()
                .filter(favor -> (favor.getExecutorId() == null && favor.getDateCreation().isAfter(
                        LocalDateTime.now().minusHours(hours).minusMinutes(minutes)
                )))
                .flatMap(favor -> {
                    LocalDateTime localDateTime = LocalDateTime.now();
                    favor.setDateCreation(localDateTime);
                    favor.setDateLastModified(localDateTime);
                    favor.setStatus(Favor.TypeStatus.IN_PROGRESS);
                    return clientService.getClientIdWhoHasMinimumOrders()
                            .map(executorId -> {
                                favor.setExecutorId(executorId);
                                return favor;
                            })
                            .flatMap(x -> favorRepository.save(favor));
                }).subscribe(favor -> log.info("Scheduled task completed"));
    }
}
