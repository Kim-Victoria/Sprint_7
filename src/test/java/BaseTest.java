import io.restassured.RestAssured;
import org.junit.BeforeClass;

import static testData.TestValues.BASE_URL;

public class BaseTest {

    @BeforeClass
    public static void setUpClass() {
        RestAssured.baseURI = BASE_URL;
    }
}
