package muffintop.cym.api.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import muffintop.cym.api.controller.enums.ResponseCode;
import muffintop.cym.api.controller.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {

    public static ResponseEntity<CommonResponse> generateResponse(ResponseCode responseCode, HttpStatus status, Object data) {

        CommonResponse response = CommonResponse.builder()
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .data(data==null ? new ArrayList<>() : data)
            .build();
        return ResponseEntity
            .status(status)
            .body(response);
    }
}
