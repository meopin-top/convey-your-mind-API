package muffintop.cym.api.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import muffintop.cym.api.controller.request.ProjectRequest;
import muffintop.cym.api.domain.Project;
import muffintop.cym.api.domain.User;
import muffintop.cym.api.exception.ExistingInviteCodeException;
import muffintop.cym.api.exception.ProjectCreateFailException;
import muffintop.cym.api.exception.ProjectDeleteFailException;
import muffintop.cym.api.exception.ProjectReadFailException;
import muffintop.cym.api.exception.ProjectUpdateFailException;
import muffintop.cym.api.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public Project getProject(Long projectId){
        return projectRepository.findByProjectId(projectId).orElseThrow( ProjectReadFailException::new);
    }

    public List<Project> getProjectByUser(User user){
        return projectRepository.findAllByUser(user).orElseThrow(
            ProjectReadFailException::new);
    }

    @Transactional
    public Project createProject(User user, ProjectRequest request){
        try{
            if(projectRepository.existsByInviteCode(request.getInviteCode())){
                throw new ExistingInviteCodeException();
            }
            Project newProject = Project.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .inviteCode(request.getInviteCode())
                .maxInviteNum(request.getMaxInviteNum())
                .destination(request.getDestination())
                .type(request.getType())
                .status('O')
                .user(user)
                .build();
            return projectRepository.save(newProject);
        }catch (Exception e){
            throw new ProjectCreateFailException();
        }
    }

    @Transactional
    public Project updateProject(User user, Long projectId, ProjectRequest request){
        try{
            Project existingProject = projectRepository.findByUserAndProjectId(user, projectId).orElseThrow(ProjectReadFailException::new);
            existingProject.update(request);
            return projectRepository.save(existingProject);
        }catch (Exception e){
            throw new ProjectUpdateFailException();
        }
    }

    @Transactional
    public void deleteProject(User user, Long projectId){
        try{
            Project existingProject = projectRepository.findByUserAndProjectId(user, projectId).orElseThrow(ProjectReadFailException::new);
            projectRepository.deleteById(projectId);
        }catch (Exception e){
            throw new ProjectDeleteFailException();
        }
    }

    public String makeInviteCode(){
        int leftLimit = 48; // 0
        int rightLimit = 122; // z
        int targetStringLength = 7;

        Random random = new Random();

        String generatedString;

        while(true){
            generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
            if(!projectRepository.existsByInviteCode(generatedString)){
                break;
            }
        }
        return generatedString;
    }

    public Project getProjectByInviteCode(String inviteCode) throws UnsupportedEncodingException {
        String decodedInviteCode = URLDecoder.decode(inviteCode, "UTF-8");
        return projectRepository.findByInviteCode(decodedInviteCode).orElseThrow(ProjectReadFailException::new);
    }
}
