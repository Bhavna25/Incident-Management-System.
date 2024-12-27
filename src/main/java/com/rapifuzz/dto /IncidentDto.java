package com.rapifuzz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class IncidentDto {

    private String incidentId;
    private String incidentType;
    private String reporterName;
    private String incidentDetails;
    private LocalDateTime reportedDateTime;
    private String priority;
    private String status;
}
