package com.yxb.video.convert;

import com.yxb.api.video.dto.VideoDTO;
import com.yxb.video.domain.entity.Video;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VideoConvert {
    VideoConvert INSTANCE = Mappers.getMapper(VideoConvert.class);
    VideoDTO toDTO(Video video);
}
