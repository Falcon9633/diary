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
@ToString(exclude = {"bandList", "teacherList", "scheduleList"})
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "band_subject",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "band_id"))
    @JsonIgnoreProperties({"studentsList", "subjectList", "scheduleList"})
    @OrderBy("name ASC")
    private Set<Band> bandList = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "teacher_subject",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id"))
    @JsonIgnoreProperties({"subjectList", "scheduleList"})
    @OrderBy("name ASC")
    private Set<Teacher> teacherList = new HashSet<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "subject")
    @JsonIgnoreProperties({"band", "subject", "teacher"})
    private Set<Schedule> scheduleList = new HashSet<>();

}
