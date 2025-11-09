package fiap.tech.challenge.online.course.feedback.deliver.serverless.payload;

public record ErrorResponse(UserTypeRequest userType, String email, String error) {
}