package com.rapifuzz.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Incident {

    @Id
    private String incidentId;
    private String incidentType;
    private String reporterName;
    private String incidentDetails;
    private LocalDateTime reportedDateTime;
    private LocalDateTime reportUpdatedTime;
    private String priority;
    private String status;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType.toUpperCase();
    }

    public void setPriority(String priority) {
        this.priority = priority.toUpperCase();
    }

    public void setStatus(String status) {
        this.status = status.toUpperCase();
    }
}
