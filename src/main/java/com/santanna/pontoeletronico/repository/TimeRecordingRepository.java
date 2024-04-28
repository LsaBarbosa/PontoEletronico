package com.santanna.pontoeletronico.repository;

import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.domain.entity.RecordWorkTime;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeRecordingRepository extends JpaRepository<RecordWorkTime, Long> {



    RecordWorkTime findTopByEmployeeAndEndOfWorkIsNullOrderByStartOfWorkDesc(Employee employee);

    List<RecordWorkTime> findByEmployeeAndStartOfWorkBetween(Employee employee, LocalDateTime startDate, LocalDateTime endDate);


    @Query("SELECT rp FROM RecordWorkTime rp WHERE rp.employee.id = :userId AND rp.endOfWork IS NULL")
    RecordWorkTime findRegistrationCheckInActive(@Param("userId") Long userId);
}



