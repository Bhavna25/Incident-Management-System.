package com.rapifuzz.controller;


import com.rapifuzz.dto.IncidentDto;
import com.rapifuzz.services.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incidents")
public class IncidentController {

    @Autowired
    private IncidentService incidentService;


    @PostMapping("/create")
    public ResponseEntity<?> createIncident(@RequestBody IncidentDto incidentDto, @RequestParam String username) {
        try{
            incidentService.createIncident(incidentDto, username);
            return ResponseEntity.ok("Incident created successfully");
        }catch (Exception e){
           throw e;
        }

    }

    @GetMapping("/userincident")
    public ResponseEntity<List<IncidentDto>> getUserIncidents(@RequestParam String username){
        try{
            List<IncidentDto> incidentsByUsername = incidentService.getIncidentsByUsername(username);
            return new ResponseEntity<>(incidentsByUsername, HttpStatus.FOUND);
        }catch (Exception e){
            throw e;
        }

    }

    @PutMapping("/{incidentId}")
    public ResponseEntity<?> updateIncident(@PathVariable String incidentId, @RequestBody IncidentDto updatedIncident, @RequestParam String username) {
        try{
            incidentService.updateIncident(incidentId, updatedIncident, username);
            return ResponseEntity.ok("Incident updated successfully");
        }catch (Exception e){
           throw e;
        }
    }



}
