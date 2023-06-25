package muffintop.cym.api.controller;

import muffintop.cym.api.component.ResponseHandler;
import muffintop.cym.api.controller.enums.ResponseCode;
import muffintop.cym.api.controller.response.CommonResponse;
import muffintop.cym.api.exception.DuplicatedIdException;
import muffintop.cym.api.exception.FileUploadFailException;
import muffintop.cym.api.exception.IncorrectPasswordException;
import muffintop.cym.api.exception.InvalidFormatPasswordException;
import muffintop.cym.api.exception.InvalidPasswordException;
import muffintop.cym.api.exception.NoFileException;
import muffintop.cym.api.exception.NonExistIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResponseExceptionController {

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

}
