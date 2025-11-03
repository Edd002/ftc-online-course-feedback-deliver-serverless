package fiap.tech.challenge.online.course.feedback.deliver.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.dao.FTCOnlineCourseFeedbackDeliverServerlessDAO;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.FeedbackRequest;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.FeedbackResponse;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.PayloadObjectMapper;

import java.security.InvalidParameterException;
import java.util.List;

public class FTCOnlineCourseFeedbackDeliverServerlessHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final FTCOnlineCourseFeedbackDeliverServerlessDAO ftcOnlineCourseFeedbackDeliverServerlessDAO;

    static {
        ftcOnlineCourseFeedbackDeliverServerlessDAO = new FTCOnlineCourseFeedbackDeliverServerlessDAO();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Requisição recebida em - FTC Online Course Feedback Deliver - Payload: " + request.getBody());
        validateAPIGatewayProxyRequestEvent(request, context);

        FeedbackRequest feedbackRequest = PayloadObjectMapper.readValue(request.getBody(), FeedbackRequest.class);
        List<FeedbackResponse> feedbackResponse = ftcOnlineCourseFeedbackDeliverServerlessDAO.getFeedbackResponse(feedbackRequest);

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(PayloadObjectMapper.writeValueAsString(feedbackResponse))
                .withIsBase64Encoded(false);
    }

    private void validateAPIGatewayProxyRequestEvent(APIGatewayProxyRequestEvent request, Context context) {
        if (!request.getHttpMethod().equals("GET")) {
            throw new InvalidParameterException("Verbo HTTP inválido.");
        }
    }
}