package muffintop.cym.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RedisProjectEntity {

    @JsonProperty("project_id")
    String projectId;

    char status;

    List<Object> contents;

}
