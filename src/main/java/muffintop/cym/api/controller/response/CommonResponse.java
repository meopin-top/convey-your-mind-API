package muffintop.cym.api.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Null 값인 필드 제외
public class CommonResponse {

    private int code;

    private String message;

    private Object data;
}
