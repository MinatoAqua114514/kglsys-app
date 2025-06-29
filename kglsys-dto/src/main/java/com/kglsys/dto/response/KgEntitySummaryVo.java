package com.kglsys.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KgEntitySummaryVo {
    private Long id;
    private String name;
    private String type;
    private String description;
}