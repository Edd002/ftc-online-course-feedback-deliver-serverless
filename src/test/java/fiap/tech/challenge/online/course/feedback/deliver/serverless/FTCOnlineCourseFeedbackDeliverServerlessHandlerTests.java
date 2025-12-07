package fiap.tech.challenge.online.course.feedback.deliver.serverless;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.mock.TestContext;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.record.FeedbackRequest;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.HttpObjectMapper;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.enumeration.UserType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FTCOnlineCourseFeedbackDeliverServerlessHandlerTests {

    @Test
    void handleRequest_AdministratorSearchFeedbackSuccess() {
        FTCOnlineCourseFeedbackDeliverServerlessHandler handler = new FTCOnlineCourseFeedbackDeliverServerlessHandler();
        TestContext context = new TestContext();
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setHttpMethod("GET");
        request.setPath("/");
        request.setQueryStringParameters(HttpObjectMapper.convertValue(new FeedbackRequest(UserType.ADMINISTRATOR, "administrador1@email.com", "123", false, "Descrição Feedback", "Comentário Feedback"), new TypeReference<>() {}));
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);
        assertEquals(200, response.getStatusCode().intValue());
    }

    @Test
    void handleRequest_TeacherSearchFeedbackSuccess() {
        FTCOnlineCourseFeedbackDeliverServerlessHandler handler = new FTCOnlineCourseFeedbackDeliverServerlessHandler();
        TestContext context = new TestContext();
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setHttpMethod("GET");
        request.setPath("/");
        request.setQueryStringParameters(HttpObjectMapper.convertValue(new FeedbackRequest(UserType.TEACHER, "teacher1@email.com", "123", false, "Descrição Feedback", "Comentário Feedback"), new TypeReference<>() {}));
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);
        assertEquals(200, response.getStatusCode().intValue());
    }

    @Test
    void handleRequest_StudentSearchFeedbackSuccess() {
        FTCOnlineCourseFeedbackDeliverServerlessHandler handler = new FTCOnlineCourseFeedbackDeliverServerlessHandler();
        TestContext context = new TestContext();
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setHttpMethod("GET");
        request.setPath("/");
        request.setQueryStringParameters(HttpObjectMapper.convertValue(new FeedbackRequest(UserType.STUDENT, "student1@email.com", "123", false, "Descrição Feedback", "Comentário Feedback"), new TypeReference<>() {}));
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);
        assertEquals(200, response.getStatusCode().intValue());
    }
}