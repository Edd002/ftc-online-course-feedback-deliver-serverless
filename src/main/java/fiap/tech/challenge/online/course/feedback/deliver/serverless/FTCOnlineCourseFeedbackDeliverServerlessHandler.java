package fiap.tech.challenge.online.course.feedback.deliver.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.property.DataSourceProperties;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.request.LoginRequest;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.response.LoginResponse;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.*;

public class FTCOnlineCourseFeedbackDeliverServerlessHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final ObjectMapper objectMapper;
    private static final DataSourceProperties dataSourceProperties;

    static {
        // Dependence initialization, database connection, constant definition, etc.
        objectMapper = new ObjectMapper();
        dataSourceProperties = new DataSourceProperties();
    }

    static {
        try (Connection connection = DriverManager.getConnection(dataSourceProperties.getJdbcUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword())) {
            if (!connection.isValid(0)) {
                throw new SQLException("Não foi possível estabelecer uma conexão com o banco de dados.");
            }

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM t_feedback");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String description = resultSet.getString("description");
                System.out.println(description);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        var logger = context.getLogger();
        try {
            logger.log("Request received on - FTC Online Course Feedback Deliver - Payload: " + request.getBody());

            var loginRequest = objectMapper.readValue(request.getBody(), LoginRequest.class);
            var isAuthorized = loginRequest.username().equalsIgnoreCase("admin") && loginRequest.password().equalsIgnoreCase("admin");
            var loginResponse = new LoginResponse(isAuthorized);

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody(objectMapper.writeValueAsString(loginResponse))
                    .withIsBase64Encoded(false);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}