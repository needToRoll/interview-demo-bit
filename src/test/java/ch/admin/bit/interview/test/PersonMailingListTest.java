package ch.admin.bit.interview.test;


import common.tau.HttpUtil;
import common.tau.TestDataHelper;
import org.awaitility.Awaitility;
import org.awaitility.Duration;
import org.junit.Test;
import org.openqa.selenium.remote.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

public class PersonMailingListTest {

    private final String CREATIONURL = "http://localhost:8083/providedPersons";
    private final String MAILINGURL = "http://localhost:8081/mailRecipients";

    @Test
    public void mailingListHasToContainNewPerson(){
        Map<String, String> testMail = new HashMap<>();
        testMail.put("email", "florian."+String.valueOf(System.currentTimeMillis())+"@bit.ch");
        String personData = TestDataHelper.getParametrizedTextFromFile("person.json", testMail);
        HttpUtil.performHttpRequestWithBody(CREATIONURL, HttpMethod.POST.name(), personData);
        Awaitility.await().atMost(Duration.TEN_SECONDS).pollInterval(Duration.FIVE_HUNDRED_MILLISECONDS).until(()-> {
            String responseData = HttpUtil.performHttpRequestWithoutBody(MAILINGURL, HttpMethod.GET.name());
            return responseData.contains(testMail.get("email"));
        });
    }
}
