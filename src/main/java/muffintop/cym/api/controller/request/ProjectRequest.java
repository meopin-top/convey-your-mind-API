package muffintop.cym.api.controller.request;

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

    private char type;

}
