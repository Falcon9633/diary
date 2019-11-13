package ua.com.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.com.entity.Note;
import ua.com.entity.Schedule;
import ua.com.entity.Student;

import java.util.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class StudentScheduleNoteDTO {

    private Student student;
    private Map<Schedule, Note> scheduleNoteMap = new LinkedHashMap<>();

    public StudentScheduleNoteDTO(Student student, Map<Schedule, Note> scheduleNoteMap) {
        this.student = student;
        this.scheduleNoteMap = scheduleNoteMap;
    }


}
