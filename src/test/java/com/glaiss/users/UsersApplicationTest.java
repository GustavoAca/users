package com.glaiss.users;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.NestedTestConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.TimeZone;

@SpringBootTest(classes = UsersApplication.class)
@NestedTestConfiguration(NestedTestConfiguration.EnclosingConfiguration.OVERRIDE)
@ExtendWith(SpringExtension.class)
@WithMockUser(username = "userId=c0a7b5f5-fc77-4afe-bf73-8213b3862bfb, username=galasdalas50@gmail.com",
        authorities = {"ROLE_ADMIN"})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public abstract class UsersApplicationTest {

    static {
        System.setProperty("spring.profiles.active", "test");
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
    }
}
