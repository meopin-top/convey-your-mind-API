package muffintop.cym.api.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import muffintop.cym.api.component.ResponseHandler;
import muffintop.cym.api.controller.enums.ResponseCode;
import muffintop.cym.api.controller.request.ProjectContentRequest;
import muffintop.cym.api.controller.request.ProjectRequest;
import muffintop.cym.api.controller.response.CommonResponse;
import muffintop.cym.api.domain.User;
import muffintop.cym.api.interceptor.Auth;
import muffintop.cym.api.repository.UserResolver;
import muffintop.cym.api.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/projects")
@RestController
public class ProjectController {

    private final ProjectService projectService;

    @Auth
    @GetMapping("")
    public ResponseEntity<CommonResponse> getProjectList(@UserResolver User user) {
        return ResponseHandler.generateResponse(ResponseCode.PROJECT_READ_SUCCESS, HttpStatus.OK,
            projectService.getProjectByUser(user));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<CommonResponse> getProjectList(@PathVariable Long projectId) {
        return ResponseHandler.generateResponse(ResponseCode.PROJECT_READ_SUCCESS, HttpStatus.OK,
            projectService.getProject(projectId));
    }

    @Auth
    @PostMapping("")
    public ResponseEntity<CommonResponse> createProject(@UserResolver User user,
        @RequestBody ProjectRequest request) {
        return ResponseHandler.generateResponse(ResponseCode.PROJECT_CREATE_SUCCESS, HttpStatus.OK,
            projectService.createProject(user, request));
    }

    @Auth
    @PutMapping("/{projectId}")
    public ResponseEntity<CommonResponse> updateProject(@UserResolver User user,
        @PathVariable Long projectId, @RequestBody ProjectRequest request) {
        return ResponseHandler.generateResponse(ResponseCode.PROJECT_UPDATE_SUCCESS, HttpStatus.OK,
            projectService.updateProject(user, projectId, request));
    }

    @Auth
    @DeleteMapping("/{projectId}")
    public ResponseEntity<CommonResponse> deleteProject(@UserResolver User user,
        @PathVariable Long projectId) {
        projectService.deleteProject(user, projectId);
        return ResponseHandler.generateResponse(ResponseCode.PROJECT_DELETE_SUCCESS, HttpStatus.OK,
            new ArrayList());
    }

    @GetMapping("/invite-code")
    public ResponseEntity<CommonResponse> makeInviteCode() {
        return ResponseHandler.generateResponse(ResponseCode.PROJECT_INVITE_CODE_SUCCESS,
            HttpStatus.OK, projectService.makeInviteCode());
    }

    @GetMapping("/invite-code/{inviteCode}")
    public ResponseEntity<CommonResponse> makeInviteCode(@PathVariable String inviteCode)
        throws UnsupportedEncodingException {
        return ResponseHandler.generateResponse(ResponseCode.PROJECT_READ_SUCCESS, HttpStatus.OK,
            projectService.getProjectByInviteCode(inviteCode));
    }

    @PostMapping("/{projectId}/enter")
    public ResponseEntity<CommonResponse> enterProject(@UserResolver User user,
        @PathVariable Long projectId) {
        projectService.enterProject(projectId, user);
        return ResponseHandler.generateResponse(ResponseCode.PROJECT_READ_SUCCESS, HttpStatus.OK,
            null);
    }

    @PostMapping("/{projectId}/contents")
    public ResponseEntity<CommonResponse> registerContent(@UserResolver User user,
        @PathVariable Long projectId, @RequestBody
        ProjectContentRequest request) {
        return ResponseHandler.generateResponse(ResponseCode.PROJECT_CONTENT_CREATE_SUCCESS,
            HttpStatus.OK, projectService.registerContent(user, projectId, request));
    }

    @Auth
    @PostMapping("/{projectId}/submit")
    public ResponseEntity<CommonResponse> submitContent(@UserResolver User user,
        @PathVariable Long projectId) {
        return ResponseHandler.generateResponse(ResponseCode.PROJECT_SUBMIT_SUCCESS, HttpStatus.OK,
            projectService.submitProject(user, projectId));
    }

    @Auth
    @GetMapping("/page")
    public ResponseEntity<CommonResponse> getMyProject(@UserResolver User user,
        @RequestParam int pageNum, @RequestParam int pageSize,
        @RequestParam(defaultValue = "E") String type) {
        return ResponseHandler.generateResponse(ResponseCode.PROJECT_READ_SUCCESS, HttpStatus.OK,
            projectService.getMyProject(user, pageNum, pageSize, type));
    }


    @Auth
    @Deprecated
    @GetMapping("/top3")
    public ResponseEntity<CommonResponse> getMyProjectTop3(@UserResolver User user) {
        return ResponseHandler.generateResponse(ResponseCode.PROJECT_READ_SUCCESS, HttpStatus.OK,
            projectService.getMyProjectTop3(user));
    }

    @PostMapping("/register")
    public ResponseEntity<CommonResponse> registerProject(@UserResolver User user,
        @RequestBody String code)
        throws UnsupportedEncodingException {
        projectService.registerProject(user, code);
        return ResponseHandler.generateResponse(ResponseCode.PROJECT_REGISTER_SUCCESS,
            HttpStatus.OK, null);

    }
}
