package com.infoshareacademy.searchengine.domain;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name = "x", query = "select p.user from Phone p where p.number='222'"),
})
public class Phone {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(nullable=false, unique=true)
    private Long id;

    private String number;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}