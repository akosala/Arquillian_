package com.infoshareacademy.searchengine.repository;

import com.infoshareacademy.searchengine.domain.Gender;
import com.infoshareacademy.searchengine.domain.Phone;
import com.infoshareacademy.searchengine.domain.User;
import org.hamcrest.CustomTypeSafeMatcher;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class UsersRepositoryImplIT {

    @EJB
    private UsersRepository usersRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(
                        UsersRepository.class, UsersRepositoryImpl.class,
                        User.class, Phone.class, Gender.class
                )
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void addUser() {
        User user = new User();
        user.setName("Paweł");
        user.setGender(Gender.MAN);
        user.setAge(25);
        user.setLogin("pawel");
        user.setSurname("Kowalski");

        usersRepository.addUser(user);

        User savedUser = entityManager.find(User.class, user.getId());
        assertThat(savedUser, notNullValue());
        assertThat(savedUser.getName(), equalTo("Paweł"));
    }

    @Test
    @UsingDataSet(value = "datasets/test.yml")
    public void getUsers() {
        List<User> usersList = usersRepository.getUsersList();

        //noinspection unchecked
        assertThat(usersList, hasItems(
                new CustomTypeSafeMatcher<User>("user 1") {
                    @Override
                    protected boolean matchesSafely(User user) {
                        return "test1".equals(user.getName());
                    }
                },
                new CustomTypeSafeMatcher<User>("user 2") {
                    @Override
                    protected boolean matchesSafely(User user) {
                        return "test2".equals(user.getName());
                    }
                }
        ));
    }
}