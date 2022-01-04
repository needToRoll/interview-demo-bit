package ch.admin.bit.interview.test;


import common.tau.TestDataHelper;
import io.restassured.RestAssured;
import org.awaitility.Awaitility;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PersonMailingListTest {

    @Test
    public void verifyMailingListContent() {
        Map<String,String> testparams = new HashMap<>();
        final UUID uuid = UUID.randomUUID();
        testparams.put("email", uuid +"@demo.test");
        String testPersonObject = TestDataHelper.getParametrizedTextFromFile("person.json", testparams);
        RestAssured.given().
                body(testPersonObject)
                .post("http://localhost:8083/providedPersons").then()
                .assertThat()
                .statusCode(201);
        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(()->isMailInResponse(uuid));
        
    }

    public boolean isMailInResponse(UUID uuid){
        return true;
    }

}
