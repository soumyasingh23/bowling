package com.bowling.service.mapper;

import com.bowling.common.dto.FrameDto;
import com.bowling.common.dto.PlayerDto;
import com.bowling.core.models.Frame;
import com.bowling.core.models.Player;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GameMapper {

    PlayerDto toDto(Player player);
    FrameDto toDto(Frame frame);
    List<FrameDto> toDtoList(List<Frame> frames);
}
