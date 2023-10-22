package muffintop.cym.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import muffintop.cym.api.controller.enums.Status;
import muffintop.cym.api.controller.request.ProjectContentRequest;
import muffintop.cym.api.controller.request.ProjectRequest;
import muffintop.cym.api.controller.response.PageResponse;
import muffintop.cym.api.controller.response.ProjectInfoResponse;
import muffintop.cym.api.domain.Project;
import muffintop.cym.api.domain.ProjectContent;
import muffintop.cym.api.domain.RedisProjectEntity;
import muffintop.cym.api.domain.User;
import muffintop.cym.api.domain.UserProjectHistory;
import muffintop.cym.api.exception.ExistingInviteCodeException;
import muffintop.cym.api.exception.InvalidInviteCodeException;
import muffintop.cym.api.exception.ProjectCreateFailException;
import muffintop.cym.api.exception.ProjectDeleteFailException;
import muffintop.cym.api.exception.ProjectReadFailException;
import muffintop.cym.api.exception.ProjectUpdateFailException;
import muffintop.cym.api.exception.UnAuthorizedException;
import muffintop.cym.api.repository.ProjectContentRepository;
import muffintop.cym.api.repository.ProjectRepository;
import muffintop.cym.api.repository.UserProjectHistoryRepository;
import muffintop.cym.api.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserProjectHistoryRepository userProjectHistoryRepository;
    private final ProjectContentRepository projectContentRepository;

    private final ObjectMapper objectMapper;

    private final RedisUtils redisUtils;

    private String URL_PATTERN = "^(?:(?:https?://)?34\\.64\\.92\\.123(?:/edit/|/view/))?(\\w+)$";

    private Pattern urlPattern = Pattern.compile(URL_PATTERN);

    public Project getProject(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(ProjectReadFailException::new);
    }

    public List<Project> getProjectByUser(User user) {
        return projectRepository.findAllByUser(user).orElseThrow(
            ProjectReadFailException::new);
    }

    @Transactional
    public Project createProject(User user, ProjectRequest request) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Parse the string to LocalDateTime
        if (projectRepository.existsByInviteCode(request.getInviteCode())) {
            throw new ExistingInviteCodeException();
        }
        try {
            Project newProject = Project.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .inviteCode(request.getInviteCode())
                .maxInviteNum(request.getMaxInviteNum())
                .destination(request.getDestination())
                .type(request.getType())
                .status('O')
                .user(user)
                .expiredDatetime(LocalDateTime.parse(request.getExpiredDatetime(), formatter))
                .build();
            newProject = projectRepository.save(newProject);
            RedisProjectEntity redisProjectEntity = RedisProjectEntity.builder()
                .projectId(newProject.getId().toString())
                .status(newProject.getStatus())
                .contents(new ArrayList<>())
                .build();
            redisUtils.setData(newProject.getId().toString(),
                objectMapper.writeValueAsString(redisProjectEntity));
            return newProject;
        } catch (Exception e) {
            throw new ProjectCreateFailException();
        }
    }

    @Transactional
    public Project updateProject(User user, Long projectId, ProjectRequest request) {
        try {
            Project existingProject = projectRepository.findByUserAndId(user, projectId)
                .orElseThrow(ProjectReadFailException::new);
            existingProject.update(request);
            return projectRepository.save(existingProject);
        } catch (Exception e) {
            throw new ProjectUpdateFailException();
        }
    }

    @Transactional
    public void deleteProject(User user, Long projectId) {
        try {
            Project existingProject = projectRepository.findByUserAndId(user, projectId)
                .orElseThrow(ProjectReadFailException::new);
            projectRepository.deleteById(projectId);
        } catch (Exception e) {
            throw new ProjectDeleteFailException();
        }
    }

    public String makeInviteCode() {
        int leftLimit = 48; // 0
        int rightLimit = 122; // z
        int targetStringLength = 7;

        Random random = new Random();

        String generatedString;

        while (true) {
            generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
            if (!projectRepository.existsByInviteCode(generatedString)) {
                break;
            }
        }
        return generatedString;
    }

    public Project getProjectByInviteCode(String inviteCode) throws UnsupportedEncodingException {
        String decodedInviteCode = URLDecoder.decode(inviteCode, "UTF-8");
        return projectRepository.findByInviteCode(decodedInviteCode)
            .orElseThrow(ProjectReadFailException::new);
    }

    public void enterProject(Long projectId, User user) {
        if (user == null) {
            return;
        }
        if (!userProjectHistoryRepository.existsByProjectIdAndUser(projectId, user)) {
            Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectReadFailException::new);
            UserProjectHistory history;
            if (project.getStatus() == 'O') {
                history = UserProjectHistory.builder()
                    .user(user)
                    .project(project)
                    .expiredDatetime(project.getExpiredDatetime())
                    .status(Status.READY)
                    .isOwner(user.equals(project.getUser()))
                    .isView(false)
                    .build();
                userProjectHistoryRepository.save(history);
            } else if (project.getStatus() == 'D') {
                history = UserProjectHistory.builder()
                    .user(user)
                    .project(project)
                    .expiredDatetime(project.getExpiredDatetime())
                    .status(Status.DELIVERED)
                    .isOwner(user.equals(project.getUser()))
                    .isView(true)
                    .build();
                userProjectHistoryRepository.save(history);
            } else {
                return;
            }
        }
    }

    @Transactional
    public ProjectContent registerContent(User user, Long projectId,
        ProjectContentRequest request) {
        if (user != null) {
            UserProjectHistory userProjectHistory = userProjectHistoryRepository.findUserProjectHistoryByProjectIdAndUser(
                projectId, user).get();
            userProjectHistory.setStatus(Status.COMPLETE);
        }
        Project project = projectRepository.findById(projectId)
            .orElseThrow(ProjectReadFailException::new);
        ProjectContent newProjectContent = ProjectContent.builder()
            .user(user != null ? user : null)
            .project(project)
            .sessionId(request.getSessionId())
            .type(request.getType())
            .content(request.getContent())
            .positionX(request.getPositionX())
            .positionY(request.getPositionY())
            .positionZ(request.getPositionZ())
            .height(request.getHeight())
            .width(request.getWidth())
            .sender(request.getSender())
            .isAnonymous(request.isAnonymous())
            .status(Status.COMPLETE.getStatus())
            .build();
        return projectContentRepository.save(newProjectContent);
    }

    @Transactional
    public Project submitProject(User user, Long projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(ProjectReadFailException::new);
        if (!user.equals(project.getUser())) {
            throw new UnAuthorizedException();
        }
        project.setStatus(Status.DELIVERED.getStatus());
        List<UserProjectHistory> historyList = userProjectHistoryRepository.findAllByProjectId(
            projectId);
        historyList.forEach(userProjectHistory -> {
            userProjectHistory.setStatus(Status.DELIVERED);
        });
        redisUtils.deleteData(projectId.toString());
        return project;
    }

    @Transactional
    public PageResponse<List<ProjectInfoResponse>> getMyProject(User user, int pageNum,
        int pageSize, String type) {

        long totalSize;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize,
            Sort.by("expiredDatetime").descending().and(Sort.by("status")));
        List<UserProjectHistory> userProjectHistoryList;
        if (type.equals("D")) {
            totalSize = userProjectHistoryRepository.countByUserAndIsViewTrue(user);
            userProjectHistoryList = userProjectHistoryRepository.findAllByUserAndView(user, true,
                pageable);
        } else {

            totalSize = userProjectHistoryRepository.countByUserAndIsViewFalse(user);
            userProjectHistoryList = userProjectHistoryRepository.findAllByUserAndView(user, false,
                pageable);

        }

        List<ProjectInfoResponse> results = new ArrayList<>();

        userProjectHistoryList.forEach(history -> {
            results.add(ProjectInfoResponse.builder()
                .id(history.getProject().getId())
                .isOwner(history.isOwner())
                .title(history.getProject().getTitle())
                .description(history.getProject().getDescription())
                .inviteCode(history.getProject().getInviteCode())
                .maxInviteNum(history.getProject().getMaxInviteNum())
                .destination(history.getProject().getDestination())
                .type(history.getProject().getType())
                .status(history.getStatus().getStatus())
                .createdDatetime(history.getProject().getCreatedDatetime())
                .updatedDatetime(history.getProject().getUpdatedDatetime())
                .expiredDatetime(history.getProject().getExpiredDatetime())
                .build()
            );
        });

        PageResponse<List<ProjectInfoResponse>> response = new PageResponse<>();
        response.setTotalLength(totalSize);
        response.setPageResult(results);
        return response;
    }


    @Transactional
    public List<ProjectInfoResponse> getMyProjectTop3(User user) {

        List<UserProjectHistory> userProjectHistoryList = userProjectHistoryRepository.findTop3ByUserOrderByExpiredDatetimeDesc(
            user);

        List<ProjectInfoResponse> results = new ArrayList<>();

        userProjectHistoryList.forEach(history -> {
            results.add(ProjectInfoResponse.builder()
                .id(history.getProject().getId())
                .isOwner(history.isOwner())
                .title(history.getProject().getTitle())
                .description(history.getProject().getDescription())
                .inviteCode(history.getProject().getInviteCode())
                .maxInviteNum(history.getProject().getMaxInviteNum())
                .type(history.getProject().getType())
                .status(history.getStatus().getStatus())
                .createdDatetime(history.getProject().getCreatedDatetime())
                .updatedDatetime(history.getProject().getUpdatedDatetime())
                .expiredDatetime(history.getProject().getExpiredDatetime())
                .build()
            );
        });
        return results;
    }

    private String findInviteCode(String code) {
        Matcher matcher = urlPattern.matcher(code);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public void registerProject(User user, String code) throws UnsupportedEncodingException {
        String inviteCode = findInviteCode(code);
        if (inviteCode == null) {
            throw new InvalidInviteCodeException();
        }

        Project project = getProjectByInviteCode(inviteCode);

        enterProject(project.getId(), user);
    }


}
