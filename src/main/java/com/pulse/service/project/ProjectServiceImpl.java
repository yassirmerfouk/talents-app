package com.pulse.service.project;

import com.pulse.dto.project.ProjectRequest;
import com.pulse.dto.project.ProjectResponse;
import com.pulse.exception.custom.EntityNotFoundException;
import com.pulse.exception.custom.ForbiddenException;
import com.pulse.mapper.ProjectMapper;
import com.pulse.model.Project;
import com.pulse.model.Talent;
import com.pulse.repository.ProjectRepository;
import com.pulse.service.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final AuthenticationService authenticationService;

    private Project getProject(Long id){
        return projectRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Project %d not found",id))
        );
    }

    private Project getAuthorizedProject(Long id){
        Project project = getProject(id);
        if(!project.getTalent().getId().equals(authenticationService.getAuthenticatedUserId()))
            throw new ForbiddenException();
        return project;
    }

    @Override
    public ProjectResponse addProject(ProjectRequest projectRequest){
        Talent talent = (Talent) authenticationService.getAuthenticatedUser();
        Project project = projectMapper.mapToProject(projectRequest);
        project.setTalent(talent);
        projectRepository.save(project);
        return projectMapper.mapToProjectResponse(project);
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest projectRequest){
        Project project = getAuthorizedProject(id);
        project.copyProperties(projectMapper.mapToProject(projectRequest));
        projectRepository.save(project);
        return projectMapper.mapToProjectResponse(project);
    }

    @Override
    public void deleteProject(Long id){
        Project project = getAuthorizedProject(id);
        projectRepository.delete(project);
    }
}
