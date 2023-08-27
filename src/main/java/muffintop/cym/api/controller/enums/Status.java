package muffintop.cym.api.controller.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    READY('R', "작성 전"),
    COMPLETE('C', "작성 완료"),
    DELIVERED('D', "전달 완료");
    private char status;
    private String description;
}
