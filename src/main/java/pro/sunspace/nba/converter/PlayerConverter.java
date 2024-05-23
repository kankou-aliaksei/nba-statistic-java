package pro.sunspace.nba.converter;

import pro.sunspace.nba.dto.player.PlayerCreateDto;
import pro.sunspace.nba.dto.player.PlayerDto;
import pro.sunspace.nba.dto.player.PlayerUpdateDto;
import pro.sunspace.nba.model.Player;

public class PlayerConverter {

    public static PlayerDto toDto(Player player) {
        return PlayerDto.builder()
                .id(player.getId())
                .name(player.getName())
                .build();
    }

    public static Player toModel(PlayerCreateDto playerDto) {
        return Player.builder()
                .name(playerDto.getName())
                .build();
    }

    public static Player toModel(long id, PlayerUpdateDto playerDto) {
        return Player.builder()
                .id(id)
                .name(playerDto.getName())
                .build();
    }
}
