package org.xproce.firesafe_audit.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsDTO {

    private String label;

    private Long count;

    private Double percentage;

    private Map<String, Object> additionalData;
}