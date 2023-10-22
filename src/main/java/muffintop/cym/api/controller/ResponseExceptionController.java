package muffintop.cym.api.controller;

import muffintop.cym.api.component.ResponseHandler;
import muffintop.cym.api.controller.enums.ResponseCode;
import muffintop.cym.api.controller.response.CommonResponse;
import muffintop.cym.api.exception.DuplicatedIdException;
import muffintop.cym.api.exception.ExistingInviteCodeException;
import muffintop.cym.api.exception.FileUploadFailException;
import muffintop.cym.api.exception.IncorrectPasswordException;
import muffintop.cym.api.exception.InvalidFormatEmailException;
import muffintop.cym.api.exception.InvalidFormatIdException;
import muffintop.cym.api.exception.InvalidFormatPasswordException;
import muffintop.cym.api.exception.InvalidPasswordException;
import muffintop.cym.api.exception.NoFileException;
import muffintop.cym.api.exception.NonExistIdException;
import muffintop.cym.api.exception.ProjectCreateFailException;
import muffintop.cym.api.exception.ProjectDeleteFailException;
import muffintop.cym.api.exception.ProjectReadFailException;
import muffintop.cym.api.exception.ProjectUpdateFailException;
import muffintop.cym.api.exception.UnAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResponseExceptionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseExceptionController.class);

    @ExceptionHandler(value = {DuplicatedIdException.class})
    protected ResponseEntity<CommonResponse> handleDuplicatedIdException() {
        return ResponseHandler.generateResponse(ResponseCode.DUPLICATED_ID, HttpStatus.OK, null);
    }

    @ExceptionHandler(value = {IncorrectPasswordException.class})
    protected ResponseEntity<CommonResponse> handleIncorrectPasswordException() {
        return ResponseHandler.generateResponse(ResponseCode.INCORRECT_PASSWORD, HttpStatus.OK,
            null);
    }

    @ExceptionHandler(value = {InvalidFormatPasswordException.class})
    protected ResponseEntity<CommonResponse> handleInvalidFormatPasswordException() {
        return ResponseHandler.generateResponse(ResponseCode.INVALID_FORMAT_PASSWORD, HttpStatus.OK,
            null);
    }

    @ExceptionHandler(value = {InvalidFormatIdException.class})
    protected ResponseEntity<CommonResponse> handleInvalidFormatIdException() {
        return ResponseHandler.generateResponse(ResponseCode.INVALID_FORMAT_ID, HttpStatus.OK,
            null);
    }

    @ExceptionHandler(value = {InvalidFormatEmailException.class})
    protected ResponseEntity<CommonResponse> handleInvalidFormatEmailException() {
        return ResponseHandler.generateResponse(ResponseCode.INVALID_FORMAT_EMAIL, HttpStatus.OK,
            null);
    }

    @ExceptionHandler(value = {NonExistIdException.class})
    protected ResponseEntity<CommonResponse> handleNonExistIdException() {
        return ResponseHandler.generateResponse(ResponseCode.NON_EXIST_ID, HttpStatus.OK, null);
    }

    @ExceptionHandler(value = {InvalidPasswordException.class})
    protected ResponseEntity<CommonResponse> handleInvalidPasswordException() {
        return ResponseHandler.generateResponse(ResponseCode.INVALID_PASSWORD, HttpStatus.OK, null);
    }

    @ExceptionHandler(value = {FileUploadFailException.class})
    protected ResponseEntity<CommonResponse> handleFileUploadFailException() {
        return ResponseHandler.generateResponse(ResponseCode.FILE_UPLOAD_FAIL, HttpStatus.OK, null);
    }

    @ExceptionHandler(value = {NoFileException.class})
    protected ResponseEntity<CommonResponse> handleNoFileException() {
        return ResponseHandler.generateResponse(ResponseCode.FILE_DELETE_FAIL, HttpStatus.OK, null);
    }

    @ExceptionHandler(value = {UnAuthorizedException.class})
    protected ResponseEntity<CommonResponse> handleUnAuthorizeException() {
        return ResponseHandler.generateResponse(ResponseCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED,
            null);
    }

    @ExceptionHandler(value = {ProjectReadFailException.class})
    protected ResponseEntity<CommonResponse> handleProjectReadFailException() {
        return ResponseHandler.generateResponse(ResponseCode.PROJECT_READ_FAIL, HttpStatus.OK,
            null);
    }

    @ExceptionHandler(value = {ProjectCreateFailException.class})
    protected ResponseEntity<CommonResponse> handleProjectCreateFailException() {
        return ResponseHandler.generateResponse(ResponseCode.PROJECT_CREATE_FAIL, HttpStatus.OK,
            null);
    }

    @ExceptionHandler(value = {ProjectUpdateFailException.class})
    protected ResponseEntity<CommonResponse> handleProjectUpdateFailException() {
        return ResponseHandler.generateResponse(ResponseCode.PROJECT_UPDATE_FAIL, HttpStatus.OK,
            null);
    }

    @ExceptionHandler(value = {ProjectDeleteFailException.class})
    protected ResponseEntity<CommonResponse> handleProjectDeleteFailException() {
        return ResponseHandler.generateResponse(ResponseCode.PROJECT_DELETE_FAIL, HttpStatus.OK,
            null);
    }

    @ExceptionHandler(value = {ExistingInviteCodeException.class})
    protected ResponseEntity<CommonResponse> handleExistingInviteCodeException() {
        return ResponseHandler.generateResponse(ResponseCode.PROJECT_INVITE_CODE_EXISTED,
            HttpStatus.OK, null);
    }

    @ExceptionHandler(value = {Exception.class})
    protected void handleException(Exception e) {
        LOGGER.error(e.toString());
    }

}
