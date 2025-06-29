package com.kglsys.dto.nodedetail;

import com.kglsys.domain.enums.ResourceType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResourceSummaryDTO {
    private Long id;
    private String title;
    private ResourceType type;
}