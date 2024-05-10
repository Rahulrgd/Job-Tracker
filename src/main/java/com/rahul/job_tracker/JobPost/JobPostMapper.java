package com.rahul.job_tracker.JobPost;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JobPostMapper {
  JobPostMapper INSTANCE = Mappers.getMapper(JobPostMapper.class);

  @Mapping(source = "id", target = "jobPostId")
  @Mapping(source = "status", target = "jobStatus")
  @Mapping(source = "user.fullName", target = "username")
  JobPostDTO toDTO(JobPost jobPost);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "resume", ignore = true)
  @Mapping(target = "resumeName", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "user", ignore = true)
  JobPost toEntity(JobPostDTO jobPostDTO);
}
