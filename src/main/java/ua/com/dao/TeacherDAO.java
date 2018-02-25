package ua.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.entity.Teacher;

public interface TeacherDAO extends JpaRepository<Teacher, Integer> {
}
