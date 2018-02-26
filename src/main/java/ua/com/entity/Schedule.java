package ua.com.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString(exclude = {"band","subject","teacher"})
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date date;
    private int numberOfLesson;
    private int dayOfWeek;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Band band;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Subject subject;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Teacher teacher;


}
