package entities;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@FilterDef(name = "perfTheatreFilter", parameters = @ParamDef(name = "theatreParam", type = "java.lang.String"))

@Entity
@Table(name = "performances")
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne(targetEntity = Theatre.class)
    @JoinColumn(name = "theatre", referencedColumnName = "id")
    @FilterJoinTable(name = "perfTheatreFilter", condition = "theatres.name like :theatreParam")
    private Theatre theatre;

    @Column(name = "duration")
    private int duration;

    public Performance(int id, String name, int theatreId, int duration) {
        this.id = id;
        this.name = name;
        this.theatre.setId(theatreId);
        this.duration = duration;
    }

    public Performance() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Theatre getTheatre() {
        return theatre;
    }

    public void setTheatre(Theatre theatre) {
        this.theatre = theatre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
