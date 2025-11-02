package fiap.tech.challenge.online.course.feedback.deliver.serverless;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.mock.TestContext;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.property.DataSourceProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FTCOnlineCourseFeedbackDeliverServerlessHandlerTests {

    private DataSourceProperties dataSourceProperties;

    @BeforeEach
    void setUp() {
        dataSourceProperties = new DataSourceProperties();
    }

    @Test
    void contextLoads() {
        var handler = new FTCOnlineCourseFeedbackDeliverServerlessHandler();
        var context = new TestContext();

        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setHttpMethod("POST");
        request.setPath("/");
        request.setBody("{\"username\": \"admin\", \"password\": \"admin\"}");

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);
        assertEquals(200, response.getStatusCode().intValue());
        assertEquals("{\"isAuthorized\":true}", response.getBody());
    }
}