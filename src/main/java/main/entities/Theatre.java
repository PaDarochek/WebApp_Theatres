package main.entities;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;

@FilterDef(name = "theatreFilter", parameters = @ParamDef(name = "nameParam", type = "java.lang.String"))
@Filter(name = "theatreFilter", condition = "name like :nameParam")

@Entity
@Table(name = "theatres")
public class Theatre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "ground_floor_rows")
    private int ground_floor_rows;

    @Column(name = "ground_floor_row_seats")
    private int ground_floor_row_seats;

    @Column(name = "balcony_rows")
    private int balcony_rows;

    @Column(name = "balcony_row_seats")
    private int balcony_row_seats;

    @Column(name = "mezzanine_rows")
    private int mezzanine_rows;

    @Column(name = "mezzanine_row_seats")
    private int mezzanine_row_seats;

    public Theatre(int id, String name, String address, int ground_floor_rows, int ground_floor_row_seats,
                   int balcony_rows, int balcony_row_seats, int mezzanine_rows, int mezzanine_row_seats) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.ground_floor_rows = ground_floor_rows;
        this.ground_floor_row_seats = ground_floor_row_seats;
        this.balcony_rows = balcony_rows;
        this.balcony_row_seats = balcony_row_seats;
        this.mezzanine_rows = mezzanine_rows;
        this.mezzanine_row_seats = mezzanine_row_seats;
    }

    public Theatre() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getGround_floor_rows() {
        return ground_floor_rows;
    }

    public void setGround_floor_rows(int ground_floor_rows) {
        this.ground_floor_rows = ground_floor_rows;
    }

    public int getGround_floor_row_seats() {
        return ground_floor_row_seats;
    }

    public void setGround_floor_row_seats(int ground_floor_row_seats) {
        this.ground_floor_row_seats = ground_floor_row_seats;
    }

    public int getBalcony_rows() {
        return balcony_rows;
    }

    public void setBalcony_rows(int balcony_rows) {
        this.balcony_rows = balcony_rows;
    }

    public int getBalcony_row_seats() {
        return balcony_row_seats;
    }

    public void setBalcony_row_seats(int balcony_row_seats) {
        this.balcony_row_seats = balcony_row_seats;
    }

    public int getMezzanine_rows() {
        return mezzanine_rows;
    }

    public void setMezzanine_rows(int mezzanine_rows) {
        this.mezzanine_rows = mezzanine_rows;
    }

    public int getMezzanine_row_seats() {
        return mezzanine_row_seats;
    }

    public void setMezzanine_row_seats(int mezzanine_row_seats) {
        this.mezzanine_row_seats = mezzanine_row_seats;
    }
}