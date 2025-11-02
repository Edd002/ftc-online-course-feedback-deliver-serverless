package fiap.tech.challenge.online.course.feedback.deliver.serverless.dao;

import fiap.tech.challenge.online.course.feedback.deliver.serverless.config.DataSourceProperties;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.FeedbackRequest;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.FeedbackResponse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FTCOnlineCourseFeedbackDeliverServerlessDAO {

    private final Connection connection;

    public FTCOnlineCourseFeedbackDeliverServerlessDAO() {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        try {
            connection = DriverManager.getConnection(dataSourceProperties.getJdbcUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword());
            if (!connection.isValid(0)) {
                throw new SQLException("Não foi possível estabelecer uma conexão com o banco de dados.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FeedbackResponse> getFeedbackResponse(FeedbackRequest feedbackRequest) {
        List<FeedbackResponse> feedbackResponse = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT tf.urgent as urgent, tf.description as description, tf.comment as comment, tt.email as teacher_email FROM public.t_feedback tf " +
                    "INNER JOIN public.t_assessment ta on ta.id = tf.fk_assessment " +
                    "INNER JOIN public.t_teacher_student tts on tts.id = ta.fk_teacher_student " +
                    "INNER JOIN public.t_teacher tt on tt.id = tts.fk_teacher " +
                    "INNER JOIN public.t_student ts on ts.id = tts.fk_student;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                feedbackResponse.add(new FeedbackResponse(
                        resultSet.getBoolean("urgent"),
                        resultSet.getString("description"),
                        resultSet.getString("comment"),
                        resultSet.getString("teacher_email")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return feedbackResponse;
    }
}