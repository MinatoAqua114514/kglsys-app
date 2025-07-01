package com.kglsys.dto.knowledgegraph.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KgEntityPropertyVo {
    private String propertyName;
    private String propertyValue;
}