package fiap.tech.challenge.online.course.feedback.deliver.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.config.KMSConfig;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.dao.FTCOnlineCourseFeedbackDeliverServerlessDAO;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.loader.ApplicationPropertiesLoader;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.HttpObjectMapper;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.record.FeedbackRequest;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.record.FeedbackResponse;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.record.error.ErrorResponse;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.record.error.InvalidParameterErrorResponse;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class FTCOnlineCourseFeedbackDeliverServerlessHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final KMSConfig kmsConfig;
    private static final Properties applicationProperties;
    private static final FTCOnlineCourseFeedbackDeliverServerlessDAO ftcOnlineCourseFeedbackDeliverServerlessDAO;

    static {
        try {
            kmsConfig = new KMSConfig();
            applicationProperties = ApplicationPropertiesLoader.loadProperties(kmsConfig);
            ftcOnlineCourseFeedbackDeliverServerlessDAO = new FTCOnlineCourseFeedbackDeliverServerlessDAO(applicationProperties);
        } catch (Exception ex) {
            System.err.println("Message: " + ex.getMessage() + " - Cause: " + ex.getCause() + " - Stacktrace: " + Arrays.toString(ex.getStackTrace()));
            throw new ExceptionInInitializerError(ex);
        }
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            FeedbackRequest feedbackRequest = HttpObjectMapper.convertValue(request.getQueryStringParameters(), FeedbackRequest.class);
            if (feedbackRequest == null) {
                context.getLogger().log("Erro de conversão de parâmetros de requisição.", LogLevel.ERROR);
                return buildInvalidParameterErrorResponse(new InvalidParameterException("Os parâmetros para consulta de feedbacks não foram informados corretamente."));
            }
            context.getLogger().log("Requisição recebida em FTC Online Course Feedback Deliver - UserType: " + feedbackRequest.userType()  + " - E-mail: " + feedbackRequest.email(), LogLevel.INFO);
            validateAPIGatewayProxyRequestEvent(feedbackRequest);
            Long userId = ftcOnlineCourseFeedbackDeliverServerlessDAO.getUserIdByEmailAndAccessKey(feedbackRequest);
            List<FeedbackResponse> feedbackResponse = ftcOnlineCourseFeedbackDeliverServerlessDAO.getFeedbackResponse(userId, feedbackRequest);
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(HttpObjectMapper.writeValueAsString(feedbackResponse)).withIsBase64Encoded(false);
        } catch (InvalidParameterException e) {
            context.getLogger().log("Message: " + e.getMessage() + " - Cause: " + e.getCause() + " - Stacktrace: " + Arrays.toString(e.getStackTrace()), LogLevel.ERROR);
            return buildInvalidParameterErrorResponse(e);
        } catch (Exception e) {
            context.getLogger().log("Message: " + e.getMessage() + " - Cause: " + e.getCause() + " - Stacktrace: " + Arrays.toString(e.getStackTrace()), LogLevel.ERROR);
            return buildErrorResponse(e);
        }
    }

    private void validateAPIGatewayProxyRequestEvent(FeedbackRequest feedbackRequest) {
        try {
            if (feedbackRequest == null || feedbackRequest.userType() == null || feedbackRequest.email() == null || feedbackRequest.accessKey() == null) {
                throw new InvalidParameterException("O tipo de usuário juntamente com seu o e-mail e chave de acesso são obrigatórios para realizar a busca de feedbacks.");
            }
        } catch (Exception e) {
            throw new InvalidParameterException(e.getMessage());
        }
    }

    private APIGatewayProxyResponseEvent buildInvalidParameterErrorResponse(InvalidParameterException e) {
        return new APIGatewayProxyResponseEvent().withStatusCode(400).withBody(HttpObjectMapper.writeValueAsString(new InvalidParameterErrorResponse(e.getMessage(), e.getCause() != null ? e.getCause().toString() : null))).withIsBase64Encoded(false);
    }

    private APIGatewayProxyResponseEvent buildErrorResponse(Exception e) {
        return new APIGatewayProxyResponseEvent().withStatusCode(500).withBody(HttpObjectMapper.writeValueAsString(new ErrorResponse(e.getMessage(), e.getCause() != null ? e.getCause().toString() : null))).withIsBase64Encoded(false);
    }
}