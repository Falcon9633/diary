package ua.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.entity.GroupStudent;

public interface GroupStudentDAO extends JpaRepository<GroupStudent,Integer> {
}
