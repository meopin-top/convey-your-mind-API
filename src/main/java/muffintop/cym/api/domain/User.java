package muffintop.cym.api.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import muffintop.cym.api.domain.key.UserPk;

@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode // 필수
@NoArgsConstructor
@AllArgsConstructor
@Table(name="USER")
@IdClass(UserPk.class)
public class User implements Serializable {

    @Id
    private String userId;

    @Id
    private char authMethod;

    private String nickName;

    private String password;

    private String refreshToken;

    private char status;

    private LocalDateTime createdDatetime;

    private LocalDateTime updatedDatetime;

}
