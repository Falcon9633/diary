package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dao.LessonDAO;
import ua.com.entity.Lesson;
import ua.com.service.LessonService;

import java.util.List;

@Service
@Transactional
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonDAO lessonDAO;

    @Override
    public List<Lesson> findAllByMonth(int monthIndex) {
        return lessonDAO.findAllByMonth(monthIndex);
    }
}
