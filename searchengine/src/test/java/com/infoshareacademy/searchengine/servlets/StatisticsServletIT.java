package com.infoshareacademy.searchengine.servlets;

import com.infoshareacademy.searchengine.dao.StatisticsRepositoryDao;
import com.infoshareacademy.searchengine.dao.StatisticsRepositoryDaoBean;
import com.infoshareacademy.searchengine.domain.Gender;
import com.infoshareacademy.searchengine.domain.Phone;
import com.infoshareacademy.searchengine.domain.User;
import com.infoshareacademy.searchengine.repository.StatisticsRepository;
import com.infoshareacademy.searchengine.repository.UsersRepository;
import com.infoshareacademy.searchengine.repository.UsersRepositoryImpl;
import org.hamcrest.CustomTypeSafeMatcher;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class StatisticsServletIT {

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(
                        StatisticsServlet.class,
                        StatisticsRepositoryDao.class, StatisticsRepositoryDaoBean.class,
                        StatisticsRepository.class,
                        UsersRepository.class, UsersRepositoryImpl.class,
                        User.class, Phone.class, Gender.class
                )
                .addAsWebResource(new File("src/main/webapp", "users-list.jsp"))
                .addAsWebResource(new File("src/main/webapp", "go-back.jsp"))
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    @UsingDataSet(value = "datasets/test.yml")
    @Cleanup(phase = TestExecutionPhase.NONE)
    @InSequence(1)
    public void setupData() {

    }

    @Test
    @InSequence(2)
    @RunAsClient
    public void getStatistics(@ArquillianResource URL url) throws IOException {
        Document statistics = Jsoup.connect(new URL(url, "statistics").toString()).get();
        //noinspection unchecked
        assertThat(statistics.select("div:gt(1)").eachText(), hasItems(
                new CustomTypeSafeMatcher<String>("user 1") {
                    @Override
                    protected boolean matchesSafely(String item) {
                        return item.contains("test1");
                    }
                },
                new CustomTypeSafeMatcher<String>("user 2") {
                    @Override
                    protected boolean matchesSafely(String item) {
                        return item.contains("test2");
                    }
                }
        ));
    }

    @Test
    @Cleanup(phase = TestExecutionPhase.BEFORE)
    @InSequence(3)
    public void cleanupData() {

    }
}