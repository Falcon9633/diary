package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dao.ScheduleDAO;
import ua.com.entity.Schedule;
import ua.com.service.ScheduleService;

import java.util.List;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleDAO scheduleDAO;

    @Override
    public void save(Schedule Schedule) {
        scheduleDAO.save(Schedule);
    }

    @Override
    public Schedule findOne(int id) {
        return scheduleDAO.findOne(id);
    }

    @Override
    public List<Schedule> findAll() {
        return scheduleDAO.findAll();
    }


}
