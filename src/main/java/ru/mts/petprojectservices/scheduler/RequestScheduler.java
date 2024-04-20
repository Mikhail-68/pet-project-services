package ru.mts.petprojectservices.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.mts.petprojectservices.entity.Request;
import ru.mts.petprojectservices.repository.RequestRepository;

import java.time.LocalDateTime;

@Service
public class RequestScheduler {

    @Value("${app.time-after-request-to-work.minutes:5}")
    private int minutes;
    @Value("${app.time-after-request-to-work.hours:0}")
    private int hours;

    private final RequestRepository requestRepository;
    private final DatabaseClient databaseClient;

    @Autowired
    public RequestScheduler(RequestRepository requestRepository, DatabaseClient databaseClient) {
        this.requestRepository = requestRepository;
        this.databaseClient = databaseClient;
    }

    @Async
    @Scheduled(fixedRateString = "${app.scheduling.check-interval}")
    public void automaticallyRequestToWork() {
        requestRepository.findAll()
                .filter(request -> (request.getExecutorId() == null && request.getDateCreation().isAfter(
                        LocalDateTime.now().minusHours(hours).minusMinutes(minutes)
                )))
                .flatMap(request -> {
                    request.setDateLastModified(LocalDateTime.now());
                    request.setStatus(Request.TypeStatus.IN_PROGRESS);
                    return databaseClient.sql("select e.id as exec_id " +
                                    "from request r right join executor e on r.executor_id = e.id " +
                                    "group by e.id " +
                                    "order by count(e.id) asc " +
                                    "limit 1 ")
                            .map((x, y) -> x.get("exec_id", Integer.class)).first()
                            .map(executorId -> {
                                request.setExecutorId(executorId);
                                return request;
                            })
                            .flatMap(x -> requestRepository.save(request));
                }).subscribe();
    }
}
