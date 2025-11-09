package fiap.tech.challenge.online.course.feedback.deliver.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.dao.FTCOnlineCourseFeedbackDeliverServerlessDAO;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.*;

import java.security.InvalidParameterException;
import java.util.List;

public class FTCOnlineCourseFeedbackDeliverServerlessHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final FTCOnlineCourseFeedbackDeliverServerlessDAO ftcOnlineCourseFeedbackDeliverServerlessDAO;

    static {
        ftcOnlineCourseFeedbackDeliverServerlessDAO = new FTCOnlineCourseFeedbackDeliverServerlessDAO();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            FeedbackRequest feedbackRequest = validateAPIGatewayProxyRequestEvent(request);
            context.getLogger().log("Requisição recebida em FTC Online Course Feedback Deliver - UserType: " + feedbackRequest.userType()  + " - E-mail: " + feedbackRequest.email(), LogLevel.INFO);
            Long userId = ftcOnlineCourseFeedbackDeliverServerlessDAO.getUserIdByEmailAndAccessKey(feedbackRequest);
            List<FeedbackResponse> feedbackResponse = ftcOnlineCourseFeedbackDeliverServerlessDAO.getFeedbackResponse(userId, feedbackRequest);
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(HttpObjectMapper.writeValueAsString(feedbackResponse)).withIsBase64Encoded(false);
        } catch (InvalidParameterException e) {
            context.getLogger().log(e.getMessage(), LogLevel.ERROR);
            return buildErrorResponse(request, 400, e);
        } catch (Exception e) {
            context.getLogger().log(e.getMessage(), LogLevel.ERROR);
            return buildErrorResponse(request, 500, e);
        }
    }

    private FeedbackRequest validateAPIGatewayProxyRequestEvent(APIGatewayProxyRequestEvent request) {
        try {
            if (!request.getHttpMethod().equals("GET")) {
                throw new InvalidParameterException("Verbo HTTP inválido.");
            }
            if (request.getQueryStringParameters() == null || request.getQueryStringParameters().get("userType") == null || request.getQueryStringParameters().get("email") == null) {
                throw new InvalidParameterException("O tipo e o e-mail de usuário são obrigatórios para realizar a busca de feedbacks.");
            }
            return HttpObjectMapper.convertValue(request.getQueryStringParameters(), FeedbackRequest.class);
        } catch (Exception e) {
            throw new InvalidParameterException(e.getMessage());
        }
    }

    private APIGatewayProxyResponseEvent buildErrorResponse(APIGatewayProxyRequestEvent request, int httpStatusCode, Exception e) {
        return new APIGatewayProxyResponseEvent().withStatusCode(httpStatusCode).withBody(HttpObjectMapper.writeValueAsString(new ErrorResponse(UserTypeRequest.valueOf(request.getQueryStringParameters().get("userType")), request.getQueryStringParameters().get("email"), e.getMessage()))).withIsBase64Encoded(false);
    }
}