package com.santanna.pontoeletronico.utils;

import com.santanna.pontoeletronico.domain.dto.DetailedTimeRecordingDto;
import com.santanna.pontoeletronico.domain.dto.OvertimeDto;
import com.santanna.pontoeletronico.domain.dto.RecordCheckoutDto;
import com.santanna.pontoeletronico.domain.entity.RecordWorkTime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class TimeFormattingUtils {

    public static RecordCheckoutDto formatRecordCheckoutDto(RecordWorkTime record) {
        LocalDateTime timeCheckOut = record.getEndOfWork() != null ? record.getEndOfWork() : LocalDateTime.now();

        Duration duration = TimeFormattingUtils.calculateDuration(record.getStartOfWork(), timeCheckOut);
        long timeWorkedInMinutes = TimeFormattingUtils.calculateTimeWorkedInMinutes(duration);
        long overtimeInMinutes = TimeFormattingUtils.calculateOvertimeInMinutes(timeWorkedInMinutes);

        // Obter apenas a hora e a data
        String endOfWorkTime = timeCheckOut.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String endOfWorkDate = timeCheckOut.toLocalDate().format(DateTimeFormatter.ISO_DATE);
        String timeWorked = formatHoursAndMinutes(timeWorkedInMinutes);
        String overtime = formatHoursAndMinutes(overtimeInMinutes);

        return new RecordCheckoutDto(
                record.getId(),
                endOfWorkTime,
                endOfWorkDate,
                timeWorked,
                overtime
        );
    }


    public static DetailedTimeRecordingDto toDetailedTimeRecordingDto(RecordWorkTime record) {
        LocalDateTime timeCheckin = record.getEndOfWork() != null ? record.getStartOfWork() : LocalDateTime.now();
        LocalDateTime timeCheckOut = record.getEndOfWork() != null ? record.getEndOfWork() : LocalDateTime.now();

        Duration duration = TimeFormattingUtils.calculateDuration(record.getStartOfWork(), timeCheckOut);
        long timeWorkedInMinutes = TimeFormattingUtils.calculateTimeWorkedInMinutes(duration);
        long overtimeInMinutes = TimeFormattingUtils.calculateOvertimeInMinutes(timeWorkedInMinutes);


        String startOfWorkTime = timeCheckin.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String endOfWorkTime = timeCheckOut.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String startOfWorkDate = timeCheckin.toLocalDate().format(DateTimeFormatter.ISO_DATE);
        String endOfWorkDate = timeCheckOut.toLocalDate().format(DateTimeFormatter.ISO_DATE);
        String timeWorked = formatHoursAndMinutes(timeWorkedInMinutes);
        String overtime = formatHoursAndMinutes(overtimeInMinutes);


        return new DetailedTimeRecordingDto(
                record.getId(),
                startOfWorkTime,
                endOfWorkTime,
                startOfWorkDate,
                endOfWorkDate,
                timeWorked,
                overtime
        );
    }

    public static OvertimeDto calculateOvertime(List<RecordWorkTime> records) {
        long totalOvertimeInMinutes = 0;

        for (RecordWorkTime registro : records) {
            long timeWorkedInMinutes = Duration.between(registro.getStartOfWork(), registro.getEndOfWork()).toMinutes();
            long overtimeInMinutes = Math.max(timeWorkedInMinutes - (1 * 1), 0); // Convertendo 8 horas para minutos
            totalOvertimeInMinutes += overtimeInMinutes;
        }

        // Formatar o tempo total de horas extras para o formato HH:mm
        String totalOvertime = formatHoursAndMinutes(totalOvertimeInMinutes);

        return new OvertimeDto(totalOvertime);
    }

    private static String formatHoursAndMinutes(long totalMinutes) {
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    public static Duration calculateDuration(LocalDateTime startTime, LocalDateTime endTime) {
        return Duration.between(startTime, endTime);
    }

    public static long calculateTimeWorkedInMinutes(Duration duration) {
        return duration.toMinutes();
    }

    public static long calculateOvertimeInMinutes(long timeWorkedInMinutes) {
        return Math.max(timeWorkedInMinutes - (1 * 1), 0);
    }

}
