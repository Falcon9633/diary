package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dao.*;
import ua.com.dto.StudentScheduleNoteDTO;
import ua.com.entity.*;
import ua.com.service.NoteService;
import ua.com.service.ScheduleService;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class NoteServiceImpl implements NoteService {
    @Autowired
    private NoteDAO noteDAO;

    @Autowired
    private LessonDAO lessonDAO;

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private BandDAO bandDAO;

    @Autowired
    private ScheduleService scheduleService;

    @Override
    public List<StudentScheduleNoteDTO> getStudentScheduleNoteDTOS(int bandId, int subjectId, int monthIndex) {
        Band selectedBand = bandDAO.findByIdWithStudents(bandId);
        List<Schedule> schedules = scheduleService.journalSchedules(subjectId, bandId, monthIndex);
        List<Integer> lessonsId = schedules.stream().map(schedule -> schedule.getLesson().getId()).collect(Collectors.toList());
        List<StudentScheduleNoteDTO> studentScheduleNoteDTOS = new ArrayList<>();
        if (lessonsId.size() != 0) {
            Set<Student> studentsSet = selectedBand.getStudentsSet();
            for (Student student : studentsSet) {
                List<Note> noteList = noteDAO.findAllByStudentAndLesson(student.getId(), lessonsId);
                Map<Schedule, Note> scheduleNoteMap = new LinkedHashMap<>();
                for (Schedule schedule : schedules) {
                    Note filteredNote = noteList.stream().filter(note -> note.getLesson().getId() == schedule.getLesson().getId()).findAny().orElse(null);
                    scheduleNoteMap.put(schedule, filteredNote);
                }
                StudentScheduleNoteDTO studentScheduleNoteDTO = new StudentScheduleNoteDTO(student, scheduleNoteMap);
                studentScheduleNoteDTOS.add(studentScheduleNoteDTO);
            }
        }
        return studentScheduleNoteDTOS;
    }

    @Override
    public List<Note> getAllStudentsNotes(Principal principal) {
        Student student = studentDAO.findByEmail(principal.getName());
        System.out.println(student);
        System.out.println(noteDAO.findAllByStudent(student.getId()));
        return noteDAO.findAllByStudent(student.getId());
    }

    @Override
    public void editJournal(Map<String, String> requestParam) {
        int lessonId = Integer.parseInt(requestParam.get("lessonId"));
        String lessonTheme = requestParam.get("lessonTheme");
        String lessonHomework = requestParam.get("lessonHomework");

        Lesson currentLesson = lessonDAO.findOne(lessonId);
        currentLesson.setTheme(lessonTheme);
        currentLesson.setHomework(lessonHomework);
        lessonDAO.save(currentLesson);

        for (String key : requestParam.keySet()) {
            if (key.contains("student")) {
                int studentId = Integer.parseInt(requestParam.get(key));
                Student currentStudent = studentDAO.findOne(studentId);
                int mark = Integer.parseInt(requestParam.get("mark-" + studentId));
                boolean absent = false;
                if (requestParam.get("absent-" + studentId) != null) {
                    absent = requestParam.get("absent-" + studentId).equals("on");
                }
                Date currentTime = new GregorianCalendar().getTime();

                if (!requestParam.get("note-" + studentId).isEmpty()) {
                    int noteId = Integer.parseInt(requestParam.get("note-" + studentId));
                    Note currentNote = noteDAO.findOne(noteId);
                    currentNote.setEditingDate(currentTime);
                    currentNote.setMark(mark);
                    currentNote.setAbsent(absent);
                    currentNote.setStudent(currentStudent);
                    currentNote.setLesson(currentLesson);
                    noteDAO.save(currentNote);
                } else {
                    Note newNote = new Note();
                    newNote.setEditingDate(currentTime);
                    newNote.setMark(mark);
                    newNote.setAbsent(absent);
                    newNote.setStudent(currentStudent);
                    newNote.setLesson(currentLesson);
                    noteDAO.save(newNote);
                }
            }
        }
    }
}
