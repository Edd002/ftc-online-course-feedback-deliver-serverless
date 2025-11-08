package fiap.tech.challenge.online.course.feedback.deliver.serverless;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.mock.TestContext;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.FeedbackRequest;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.PayloadObjectMapper;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.UserTypeRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FTCOnlineCourseFeedbackDeliverServerlessHandlerTests {

    @Test
    void handleRequest_Success() {
        FTCOnlineCourseFeedbackDeliverServerlessHandler handler = new FTCOnlineCourseFeedbackDeliverServerlessHandler();
        TestContext context = new TestContext();

        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setHttpMethod("GET");
        request.setPath("/");
        request.setBody(PayloadObjectMapper.writeValueAsString(new FeedbackRequest(UserTypeRequest.TEACHER, "teacher1@email.com", "123", false, "Descrição Feedback", "Comentário Feedback")));

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);
        assertEquals(200, response.getStatusCode().intValue());
    }
}