package pro.sunspace.nba.repository.api;

import pro.sunspace.nba.model.Player;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PlayerRepository extends ReactiveCrudRepository<Player, Long> {
}
