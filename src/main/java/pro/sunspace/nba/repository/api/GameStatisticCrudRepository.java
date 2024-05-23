package pro.sunspace.nba.repository.api;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pro.sunspace.nba.model.GameStatistic;

@Repository
public interface GameStatisticCrudRepository extends ReactiveCrudRepository<GameStatistic, Long> {
}
