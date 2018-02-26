package ua.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.entity.Schedule;

public interface ScheduleDAO extends JpaRepository<Schedule, Integer> {
}
