package muffintop.cym.api.domain.key;

import java.io.Serializable;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class UserPk implements Serializable {

    private String userId;

    private char authMethod;
}
