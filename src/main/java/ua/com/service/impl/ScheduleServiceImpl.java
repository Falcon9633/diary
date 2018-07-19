package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dao.ScheduleDAO;
import ua.com.entity.Schedule;
import ua.com.service.ScheduleService;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleDAO scheduleDAO;

    @Override
    public void save(Schedule schedule) {
        scheduleDAO.save(schedule);
    }

    @Override
    public Schedule findOne(int id) {
        return scheduleDAO.findOne(id);
    }

    @Override
    public List<Schedule> findAll() {
        return scheduleDAO.findAll();
    }

    @Override
    public List<Schedule> findAllByWeekOfYearWithAllNested(int weekOfYear) {
        return scheduleDAO.findAllByWeekOfYearWithAllNested(weekOfYear);
    }

    @Override
    public List<Schedule> findAllByDate(Date startDate) {
        return scheduleDAO.findAllByDate(startDate);
    }

    @Override
    public void delete(Schedule schedule) {
        scheduleDAO.delete(schedule);
    }
}
