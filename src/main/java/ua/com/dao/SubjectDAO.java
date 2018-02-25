package ua.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.entity.Subject;

public interface SubjectDAO extends JpaRepository<Subject, Integer> {
}
