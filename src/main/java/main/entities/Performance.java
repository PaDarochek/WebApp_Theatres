package main.entities;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.FilterJoinTable;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@FilterDefs({
        @FilterDef(name = "perfTheatreFilter", parameters = @ParamDef(name = "theatreParam", type = "java.lang.String")),
        @FilterDef(name = "perfSessionsDateFilter", parameters = {@ParamDef(name = "minDate", type = "java.sql.Timestamp"),
                @ParamDef(name = "maxDate", type = "java.sql.Timestamp")})
})

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
    @FilterJoinTable(name = "perfTheatreFilter", condition = "theatre.name like :theatreParam")
    private Theatre theatre;

    @Column(name = "duration")
    private int duration;

    @OneToMany(targetEntity = Session.class, mappedBy = "performance")
    @FilterJoinTable(name = "perfSessionsDateFilter", condition = "date_time BETWEEN DATE :minDate AND DATE :maxDate")
    private List<Session> sessions = new ArrayList<Session>();

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
