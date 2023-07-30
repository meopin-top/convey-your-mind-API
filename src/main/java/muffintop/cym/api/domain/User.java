package muffintop.cym.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import muffintop.cym.api.domain.key.UserPk;

@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode // Multi Column PK에서 필수
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER")
@IdClass(UserPk.class)
public class User implements Serializable {

    @Id
    private String userId;

    @Id
    private char authMethod;

    private String email;

    private String nickName;

    @Default
    private String profile = "https://storage.googleapis.com/convey-your-mind-dev-bucket/profile/default.jpg";

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String refreshToken;

    @JsonIgnore
    private char status;

    @JsonIgnore
    private LocalDateTime createdDatetime;

    @JsonIgnore
    private LocalDateTime updatedDatetime;

}
