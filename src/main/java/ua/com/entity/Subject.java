package ua.com.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"bandSet", "teacherSet", "scheduleSet"})
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "band_subject",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "band_id"))
    @JsonIgnoreProperties({"studentsSet", "subjectSet", "scheduleSet"})
    @OrderBy("name ASC")
    private Set<Band> bandSet = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "teacher_subject",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id"))
    @JsonIgnoreProperties({"subjectSet", "scheduleSet"})
    @OrderBy("name ASC")
    private Set<Teacher> teacherSet = new HashSet<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "subject")
    @JsonIgnoreProperties({"band", "subject", "teacher", "lesson"})
    private Set<Schedule> scheduleSet = new HashSet<>();

}
