package ru.mts.petprojectservices.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.mts.petprojectservices.entity.Favor;
import ru.mts.petprojectservices.repository.FavorRepository;

import java.time.LocalDateTime;

@Service
@Slf4j
public class FavorScheduler {

    @Value("${app.time-after-request-to-work.minutes:5}")
    private int minutes;
    @Value("${app.time-after-request-to-work.hours:0}")
    private int hours;

    private final FavorRepository favorRepository;
    private final DatabaseClient databaseClient;

    @Autowired
    public FavorScheduler(FavorRepository favorRepository, DatabaseClient databaseClient) {
        this.favorRepository = favorRepository;
        this.databaseClient = databaseClient;
    }

    @Async
    @Scheduled(fixedRateString = "${app.scheduling.check-interval}")
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
                    return databaseClient.sql("select e.id as exec_id " +
                                    "from favor f right join executor e on f.executor_id = e.id " +
                                    "group by e.id " +
                                    "order by count(f.id) asc " +
                                    "limit 1 ")
                            .map((x, y) -> x.get("exec_id", Integer.class)).first()
                            .map(executorId -> {
                                favor.setExecutorId(executorId);
                                return favor;
                            })
                            .flatMap(x -> favorRepository.save(favor));
                }).subscribe();
    }
}
