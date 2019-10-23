package ua.com.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString(exclude = {"band", "subject", "teacher", "lesson"})
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @DateTimeFormat(pattern = "YYYY/MM/DD")
    private Date editingDate;
    private int weekOfYear;
    private int dayOfWeek;
    private int numberOfLesson;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"studentsSet", "subjectSet", "scheduleSet"})
    private Band band;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"bandSet", "teacherSet", "scheduleSet"})
    private Subject subject;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"subjectSet", "scheduleSet"})
    private Teacher teacher;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"schedule", "noteList"})
    private Lesson lesson;
}
