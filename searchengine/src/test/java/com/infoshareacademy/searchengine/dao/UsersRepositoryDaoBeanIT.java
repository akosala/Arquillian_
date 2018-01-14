package com.infoshareacademy.searchengine.dao;

import com.infoshareacademy.searchengine.domain.Gender;
import com.infoshareacademy.searchengine.domain.Phone;
import com.infoshareacademy.searchengine.domain.User;
import com.infoshareacademy.searchengine.interceptors.AddUserInterceptor;
import com.infoshareacademy.searchengine.interceptors.AddUserSetGenderInterceptor;
import com.infoshareacademy.searchengine.repository.UsersRepository;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(Arquillian.class)
public class UsersRepositoryDaoBeanIT {
    @Inject
    private UsersRepositoryDao usersRepositoryDao;

    @Deployment
    public static Archive<?> createDeployment() {
        File[] mockito = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .resolve("org.mockito:mockito-core")
                .withTransitivity()
                .asFile();

        return ShrinkWrap.create(WebArchive.class)
                .addClasses(
                        UsersRepositoryDao.class, UsersRepositoryDaoBean.class, UsersRepositoryDaoRemote.class,
                        AddUserInterceptor.class, AddUserSetGenderInterceptor.class,
                        UsersRepository.class, User.class, Phone.class, Gender.class
                )
                .addAsLibraries(mockito)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Produces
    public UsersRepository getUsersRepository() {
        return mock(UsersRepository.class);
    }

    @Test
    public void testAddUser() {
        User user = new User();
        user.setName("Mateusz");

        usersRepositoryDao.addUser(user);

        assertThat(user.getGender(), equalTo(Gender.MAN));
    }
}