package muffintop.cym.api.controller.response;

import java.time.LocalDateTime;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProjectInfoResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isOwner;

    private String title;

    private String description;

    private String inviteCode;

    private int maxInviteNum;

    private String destination;

    private char type;

    private char status;

    private LocalDateTime createdDatetime;

    private LocalDateTime updatedDatetime;

    private LocalDateTime expiredDatetime;


}
