package muffintop.cym.api.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class StatusResponse {

    String status;
}
