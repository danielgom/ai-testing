package com.mna.aipj.repository;

import com.mna.aipj.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Integer> {

    @Query(nativeQuery = true, value = """
            SELECT * FROM alerts a 
            WHERE a.active = false 
              AND a.trigger_date IS NULL 
              AND a.deactivated_at IS NULL 
              AND a.alert_level <> CAST(:alertLevel AS alert_level_enum)
            """)
    List<Alert> findByActiveFalseAndTriggerDateNullAndDeactivatedAtNullAndAlertLevelNot(String alertLevel);

    @Modifying
    @Query(nativeQuery = true, value = """
            UPDATE alerts
            SET trigger_date = :triggerDate, active = true
            WHERE id = :alertId
            """)
    void updateTriggerDateAndSetActiveTrue(LocalDateTime triggerDate, Integer alertId);

}
