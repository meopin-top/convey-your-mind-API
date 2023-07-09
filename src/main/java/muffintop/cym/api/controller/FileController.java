package muffintop.cym.api.controller;


import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import muffintop.cym.api.component.ResponseHandler;
import muffintop.cym.api.controller.enums.ResponseCode;
import muffintop.cym.api.controller.response.CommonResponse;
import muffintop.cym.api.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    @PostMapping("")
    public ResponseEntity<CommonResponse> upload(@RequestParam(value = "image") MultipartFile image)
        throws IOException {
        String uuid = UUID.randomUUID().toString();
        String url = fileService.makeUrl(uuid);
        fileService.upload(image, uuid);
        return ResponseHandler.generateResponse(ResponseCode.FILE_UPLOAD_SUCCESS, HttpStatus.OK,
            url);
    }

    @DeleteMapping("/{objectName}")
    public ResponseEntity<CommonResponse> upload(@PathVariable String objectName)
        throws IOException {
        fileService.delete(objectName);
        return ResponseHandler.generateResponse(ResponseCode.FILE_DELETE_SUCCESS, HttpStatus.OK,
            null);
    }

}
