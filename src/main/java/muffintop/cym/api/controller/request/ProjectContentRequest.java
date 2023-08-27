package muffintop.cym.api.controller.request;

import lombok.Data;

@Data
public class ProjectContentRequest {

    private String sessionId;

    private char type;

    private String content;

    private float positionX;

    private float positionY;

    private int positionZ;

    private float height;

    private float width;

    private String sender;

    private boolean isAnonymous;

}
