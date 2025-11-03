package fiap.tech.challenge.online.course.feedback.deliver.serverless.dao;

import fiap.tech.challenge.online.course.feedback.deliver.serverless.config.DataSourceProperties;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.FeedbackRequest;
import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.FeedbackResponse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
            String conditional = switch (feedbackRequest.userTypeRequest()) {
                case ADMINISTRATOR -> "tadmin.id = " + getUserByEmailAndAccessKey(feedbackRequest);
                case TEACHER -> "ts.id = " + getUserByEmailAndAccessKey(feedbackRequest);
                case STUDENT -> "tt.id = " + getUserByEmailAndAccessKey(feedbackRequest);
            };
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT tf.urgent as urgent, tf.description as description, tf.comment as comment, tt.email as teacher_email FROM public.t_feedback tf " +
                            "INNER JOIN public.t_assessment ta on ta.id = tf.fk_assessment " +
                            "INNER JOIN public.t_teacher_student tts on tts.id = ta.fk_teacher_student " +
                            "INNER JOIN public.t_teacher tt on tt.id = tts.fk_teacher " +
                            "INNER JOIN public.t_student ts on ts.id = tts.fk_student " +
                            "INNER JOIN public.t_administrator tadmin on tadmin.id = tt.fk_administrator " +
                            "WHERE " + conditional);

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

    public Long getUserByEmailAndAccessKey(FeedbackRequest feedbackRequest) {
        try {
            PreparedStatement preparedStatement = switch (feedbackRequest.userTypeRequest()) {
                case ADMINISTRATOR -> connection.prepareStatement("SELECT id FROM t_administrator WHERE email = ? AND access_key = ?");
                case TEACHER -> connection.prepareStatement("SELECT id FROM t_teacher WHERE email = ? AND access_key = ?");
                case STUDENT -> connection.prepareStatement("SELECT id FROM t_student WHERE email = ? AND access_key = ?");
            };
            preparedStatement.setString(1, feedbackRequest.getEmail());
            preparedStatement.setString(2, feedbackRequest.getAccessKey());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new NoSuchElementException("Nenhum usuário encontrado com as credenciais infommadas foi encontrado para realizar a busca de feedbacks.");
            }
            return resultSet.getLong("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}