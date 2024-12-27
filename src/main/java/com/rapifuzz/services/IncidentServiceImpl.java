package com.rapifuzz.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rapifuzz.dto.IncidentDto;
import com.rapifuzz.entities.Incident;
import com.rapifuzz.entities.User;
import com.rapifuzz.exceptions.BusinessException;
import com.rapifuzz.repositories.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class IncidentServiceImpl implements IncidentService{

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper mapper;

    public IncidentServiceImpl(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Incident createIncident(IncidentDto incidentDto, String username) {
        try{
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new BusinessException("User not found"));
            validateField(incidentDto);
            Incident incident = mapper.convertValue(incidentDto, Incident.class);

            incident.setUser(user);
            incident.setReportedDateTime(LocalDateTime.now());
            incident.setReportUpdatedTime(LocalDateTime.now());
            incident.setIncidentId(generateIncidentId());
            return incidentRepository.save(incident);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<IncidentDto> getIncidentsByUsername(String username) {
        try{
            List<Incident> byReporterName = incidentRepository.findByReporterName(username);
            return byReporterName.stream().map(incident -> mapper.convertValue(incident,IncidentDto.class)).collect(Collectors.toList());
        }catch (Exception e){
            throw new BusinessException("Something went wrong");
        }
    }

    @Override
    public Incident  updateIncident(String id, IncidentDto updatedIncident, String username) {
        try {
            Incident incident = incidentRepository.findByIncidentId(id)
                    .orElseThrow(() -> new BusinessException("Incident not found"));

            if (!incident.getUser().getUsername().equals(username)) {
                throw new BusinessException("You can only edit your own incidents");
            }

            if (incident.getStatus().equalsIgnoreCase("Closed")) {
                throw new BusinessException("Closed incidents cannot be edited");
            }
            validateField(updatedIncident);

            incident.setIncidentDetails(updatedIncident.getIncidentDetails());
            incident.setPriority(updatedIncident.getPriority());
            incident.setStatus(updatedIncident.getStatus());
            incident.setIncidentType(updatedIncident.getIncidentType());
            incident.setReportUpdatedTime(LocalDateTime.now());

            return incidentRepository.save(incident);

        }catch (Exception e){
            throw new BusinessException("Something went wrong");
        }
    }


    /*private String generateIncidentId() {
        try{
            Random random = new Random();
            boolean isExist = false;
            String id;
            do{
                int randomNumber = random.nextInt(90000) + 10000;
                id = "RMG" + randomNumber + LocalDateTime.now().getYear();//99999
                Optional<Incident> byIncidentId = incidentRepository.findByIncidentId(id);
                if(byIncidentId.isPresent())
                    isExist = true;
            }while(isExist);

            return id;
        }catch (Exception e){
            throw new BusinessException("Something went wrong while generating Incident ID");
        }
    }*/

    private String generateIncidentId() {
        try{
            Optional<String> lastSequence = incidentRepository.findLastSequence();
            System.out.println(lastSequence);
            if (lastSequence.isPresent()) {
                String idFromDB = lastSequence.get();
                String idWithYear = idFromDB.substring(3);
                String sequence = idWithYear.substring(0, idWithYear.length() - 4);
                long nextNumber = Long.parseLong(sequence) + 1;
                return "RMG" + String.format("%05d", nextNumber) + LocalDate.now().getYear();
            } else {
                return "RMG00000" + LocalDate.now().getYear();
            }

        }catch (Exception e){
            throw new BusinessException("Something went wrong while generating Incident ID");
        }
    }

    private void validateField(IncidentDto dto){
        if(dto.getIncidentType() == null || dto.getIncidentType().trim().isEmpty() || !incidentType(dto.getIncidentType())){
            throw new BusinessException("Incident Type should be INDIVIDUAL/ENTERPRISE/GOVERNMENT");
        }
        if(dto.getPriority()==null || dto.getPriority().trim().isEmpty() || !priorityCheck(dto.getPriority())){
            throw new BusinessException("Priority should be  HIGH/MEDIUM/LOW");
        }
        if (dto.getStatus() == null || dto.getStatus().trim().isEmpty() || !statusCheck(dto.getStatus())){
            throw new BusinessException("Status should be OPEN/IN_PROGRESS/CLOSED");
        }
    }

    private boolean incidentType(String type){
        if(type.equalsIgnoreCase("INDIVIDUAL") || type.equalsIgnoreCase("GOVERNMENT") || type.equalsIgnoreCase("ENTERPRISE")){
            return true;
        }
        return false;
    }
    private boolean priorityCheck(String prior){
        if(prior.equalsIgnoreCase("HIGH") || prior.equalsIgnoreCase("MEDIUM") || prior.equalsIgnoreCase("LOW")){
            return true;
        }
        return false;
    }
    private boolean statusCheck(String status){
        if(status.equalsIgnoreCase("OPEN") || status.equalsIgnoreCase("IN_PROGRESS") || status.equalsIgnoreCase("CLOSED")){
            return true;
        }
        return false;
    }
}
