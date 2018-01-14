package com.infoshareacademy.searchengine.repository;

import com.infoshareacademy.searchengine.domain.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class UsersRepositoryImpl implements UsersRepository {

    @PersistenceContext(unitName = "pUnit")
    private EntityManager entityManager;

    @Override
    public boolean addUser(User user) {
        entityManager.persist(user);
        System.out.println("User " + user + " added");
        return true;
    }

    @Override
    public User getUserById(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getUserByLogin(String login) {
        return (User) entityManager.createNamedQuery("getUserByLogin")
                .setParameter("login", login)
                .getSingleResult();
    }

    @Override
    public List<User> getUsersList() {
        return entityManager.createNamedQuery("getAll")
                .getResultList();
    }
}
