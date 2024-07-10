package pro.sunspace.nba.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import pro.sunspace.nba.model.GameStatistic;

public class GameStatisticSerializer implements Serializer<GameStatistic> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, GameStatistic data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize GameStatisticDto", e);
        }
    }
}
