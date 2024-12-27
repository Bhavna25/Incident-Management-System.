package com.rapifuzz.repositories;

import com.rapifuzz.entities.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incident,Long> {
    List<Incident> findByReporterName(String username);
    Optional<Incident> findByIncidentId(String incidentId);

    @Query("Select s.incidentId from Incident s order by s.reportedDateTime desc limit 1")
    Optional<String> findLastSequence();
}
