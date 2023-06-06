package muffintop.cym.api.component;

import java.util.HashMap;
import java.util.Map;
import muffintop.cym.api.controller.enums.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {

    public static ResponseEntity<Object> generateResponse(ResponseCode responseCode, HttpStatus status, Object responseObj) {
        Map<String, Object> object = new HashMap<String, Object>();
        object.put("status", responseCode.getCode());
        object.put("message", responseCode.getMessage());
        object.put("data", responseObj);
        return ResponseEntity
            .status(status)
            .body(object);
    }
}
