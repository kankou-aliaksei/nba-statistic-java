package pro.sunspace.nba.repository.api;

import pro.sunspace.nba.model.GameStatistic;
import reactor.core.publisher.Mono;

public interface GameStatisticRepository {
    Mono<Void> save(GameStatistic gameStatistic);
}
