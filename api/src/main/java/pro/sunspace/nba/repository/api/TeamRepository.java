package pro.sunspace.nba.repository.api;

import pro.sunspace.nba.model.Team;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface TeamRepository extends ReactiveCrudRepository<Team, UUID> {
}
