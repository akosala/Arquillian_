package com.infoshareacademy.searchengine.servlets;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class HelloServletIT {

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(HelloServlet.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    @RunAsClient
    public void getHelloMessage(@ArquillianResource URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url, "hello-servlet").openConnection();
        connection.setRequestMethod("GET");

        assertThat(connection.getResponseCode(), equalTo(200));
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String result = buffer.lines().collect(Collectors.joining(""));
            assertThat(result, equalTo("Hello World!"));
        }
    }
}