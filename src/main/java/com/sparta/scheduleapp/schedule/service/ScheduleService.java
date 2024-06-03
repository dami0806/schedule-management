package com.sparta.scheduleapp.schedule.service;

import com.sparta.scheduleapp.exception.*;
import com.sparta.scheduleapp.exception.message.file.FileNotSaveException;
import com.sparta.scheduleapp.exception.message.file.FileTypeNotAllowedException;
import com.sparta.scheduleapp.file.service.FileService;
import com.sparta.scheduleapp.schedule.dto.ScheduleRequestDto;
import com.sparta.scheduleapp.exception.message.ErrorMessage;
import com.sparta.scheduleapp.file.entity.File;
import com.sparta.scheduleapp.schedule.repository.FileRepository;
import com.sparta.scheduleapp.schedule.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.sparta.scheduleapp.schedule.entity.Schedule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface ScheduleService {

    Schedule createSchedule(ScheduleRequestDto requestDto, MultipartFile file, String username);
    List<Schedule> getScheduleList();
    Schedule getDetailSchedule(Long id);
    boolean verifyPassword(Long id, String password);

    Schedule updateSchedule(Long id, ScheduleRequestDto requestDto, MultipartFile file, String username);
    String deleteSchedule(Long id, String username);

    Schedule findScheduleById(Long id);


}