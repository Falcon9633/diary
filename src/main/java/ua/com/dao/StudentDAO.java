package ua.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.com.entity.GroupStudent;
import ua.com.entity.Student;

public interface StudentDAO extends JpaRepository<Student,Integer> {

}
