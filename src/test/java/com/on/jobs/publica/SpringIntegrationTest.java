package com.on.jobs.publica;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = OnJobsLegacyApplication.class)
@ContextConfiguration
@ActiveProfiles(value = "test")
public class SpringIntegrationTest {

}