package fiap.tech.challenge.online.course.feedback.deliver.serverless;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.mock.TestContext;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.FeedbackRequest;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.PayloadObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FTCOnlineCourseFeedbackDeliverServerlessHandlerTests {

    @Test
    void handleRequest_Success() {
        FTCOnlineCourseFeedbackDeliverServerlessHandler handler = new FTCOnlineCourseFeedbackDeliverServerlessHandler();
        TestContext context = new TestContext();

        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setHttpMethod("POST");
        request.setPath("/");
        request.setBody(PayloadObjectMapper.writeValueAsString(new FeedbackRequest("teacher@email.com", "123", false, "New Feedback", "New Comment")));

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);
        assertEquals(200, response.getStatusCode().intValue());
    }
}