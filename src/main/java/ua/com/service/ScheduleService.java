package ua.com.service;

import ua.com.entity.Schedule;

import java.util.List;


public interface ScheduleService {
    void save(Schedule schedule);
    Schedule findOne(int id);
    List<Schedule> findAll();

}
