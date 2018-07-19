package ua.com.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "band")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private String email;
    private String password;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"studentsList", "subjectList", "scheduleList"})
    private Band band;

//    public void setBand(Band band) {
//        setBand(band, true);
//    }
//
//    void setBand(Band band, boolean add){
//        this.band = band;
//        if (band != null && add){
//            band.addStudent(this, false);
//        }
//    }
}
