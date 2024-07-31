package com.pulse.service.project;

import com.pulse.dto.project.ProjectRequest;
import com.pulse.dto.project.ProjectResponse;

public interface ProjectService {


    ProjectResponse addProject(ProjectRequest projectRequest);

    ProjectResponse updateProject(Long id, ProjectRequest projectRequest);

    void deleteProject(Long id);
}
