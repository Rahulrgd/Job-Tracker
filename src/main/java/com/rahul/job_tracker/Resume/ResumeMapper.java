package com.rahul.job_tracker.Resume;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.rahul.job_tracker.Resume.ResumeDTOs.ResumeDTO;
import com.rahul.job_tracker.Resume.ResumeDTOs.ResumeFileDTO;

@Mapper
public interface ResumeMapper {
    ResumeMapper INSTANCE = Mappers.getMapper(ResumeMapper.class);

    ResumeDTO toDTO(Resume resume);

    @Mapping(target = "resumeFile", ignore = true)
    @Mapping(target = "resume", ignore = true)
    @Mapping(target = "user", ignore = true)
    Resume toEntity(ResumeDTO resumeDTO);

    ResumeFileDTO toFileDTO(Resume resume);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "resume", ignore = true)
    @Mapping(target = "resumeName", ignore = true)
    @Mapping(target = "user", ignore = true)
    Resume toResumeFromFile(ResumeFileDTO resumeFileDTO);
}
