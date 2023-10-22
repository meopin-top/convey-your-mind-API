package muffintop.cym.api.controller.response;

import lombok.Data;

@Data
public class PageResponse<T> {

    private long totalLength;

    private T pageResult;

}
