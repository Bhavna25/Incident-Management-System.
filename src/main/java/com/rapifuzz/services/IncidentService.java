package com.rapifuzz.services;

import com.rapifuzz.dto.IncidentDto;
import com.rapifuzz.entities.Incident;

import java.util.List;

public interface IncidentService {
    Incident createIncident(IncidentDto incidentDto, String username);

    List<IncidentDto> getIncidentsByUsername(String username);
    Incident updateIncident(String incidentId, IncidentDto updatedIncident, String username);


}
