package com.infoshareacademy.searchengine.dao;

import com.infoshareacademy.searchengine.domain.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class UsersRepositoryDaoBeanIT {

    @EJB
    private UsersRepositoryDao usersRepositoryDao;
    @PersistenceContext(name = "pUnit")
    private EntityManager em;



    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addClasses();
    }

    @Test
    public void addUser() throws Exception {
        User user = new User();
        usersRepositoryDao.addUser(user);

        List<User> users = em.createQuery("select u from User u", User.class).getResultList();

        List<User> usersList = usersRepositoryDao.getUsersList();

        assertThat(usersList.size(), is(1));
    }
}