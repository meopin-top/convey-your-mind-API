package muffintop.cym.api.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProjectRequest {

    private String title;

    private String description;

    private String inviteCode;

    private int maxInviteNum;

    private String destination;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String expiredDatetime;

    private char type;

}
