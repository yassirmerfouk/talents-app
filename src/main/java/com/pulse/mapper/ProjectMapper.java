package com.pulse.mapper;

import com.pulse.dto.project.ProjectRequest;
import com.pulse.dto.project.ProjectResponse;
import com.pulse.model.Project;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public Project mapToProject(ProjectRequest projectRequest){
        Project project = new Project();
        BeanUtils.copyProperties(projectRequest, project);
        return project;
    }

    public ProjectResponse mapToProjectResponse(Project project){
        ProjectResponse projectResponse = new ProjectResponse();
        BeanUtils.copyProperties(project, projectResponse);
        return projectResponse;
    }
}
