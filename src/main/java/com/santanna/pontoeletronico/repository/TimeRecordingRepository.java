package com.santanna.pontoeletronico.repository;

import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.domain.entity.TimeRecording;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeRecordingRepository extends JpaRepository<TimeRecording, Long> {
    TimeRecording findTopByEmployeeAndEndOfWorkIsNullOrderByStartOfWorkDesc(Employee employee);

    List<TimeRecording> findByEmployeeAndStartOfWorkBetween(Employee employee, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT rp FROM TimeRecording rp WHERE rp.employee.name = :name AND rp.endOfWork IS NULL")
    TimeRecording findRegistrationCheckInActive(@Param("name") String name);

}




