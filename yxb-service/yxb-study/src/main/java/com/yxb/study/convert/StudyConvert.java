package com.yxb.study.convert;

import com.yxb.api.study.dto.StudyProgressDTO;
import com.yxb.api.study.dto.WordBookDTO;
import com.yxb.study.domain.entity.StudyProgress;
import com.yxb.study.domain.entity.WordBook;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudyConvert {
    StudyConvert INSTANCE = Mappers.getMapper(StudyConvert.class);
    StudyProgressDTO toProgressDTO(StudyProgress progress);
    WordBookDTO toWordBookDTO(WordBook wordBook);
}
