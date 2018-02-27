package ua.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.entity.Student;

public interface StudentDAO extends JpaRepository<Student,Integer> {

}
