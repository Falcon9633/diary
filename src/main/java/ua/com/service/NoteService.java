package ua.com.service;

import ua.com.dto.StudentScheduleNoteDTO;
import ua.com.entity.Note;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface NoteService {
    List<StudentScheduleNoteDTO> getStudentScheduleNoteDTOS(int bandId, int subjectId, int monthIndex);

    List<Note> getAllStudentsNotes(Principal principal);

    void editJournal(Map<String,String> requestParam);
}
