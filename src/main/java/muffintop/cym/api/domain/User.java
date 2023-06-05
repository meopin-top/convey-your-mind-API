package muffintop.cym.api.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Data;
import muffintop.cym.api.domain.key.UserPk;

@Data
@Entity
@Table(name="USER")
@IdClass(UserPk.class)
public class User {

    @Id
    private String userId;

    @Id
    private char authMethod;

    private String nickName;

    private char status;

    private LocalDateTime createdDatetime;

    private LocalDateTime updatedDatetime;

}
