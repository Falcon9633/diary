package ua.com.service;

import ua.com.dto.StudentScheduleNoteDTO;

import java.util.List;
import java.util.Map;

public interface NoteService {
    List<StudentScheduleNoteDTO> getStudentScheduleNoteDTOS(int bandId, int subjectId, int monthIndex);

    void editJournal(Map<String,String> requestParam);
}
