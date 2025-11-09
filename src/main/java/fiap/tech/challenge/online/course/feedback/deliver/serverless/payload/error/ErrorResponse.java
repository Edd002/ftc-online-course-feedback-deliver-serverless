package fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.error;

import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.UserTypeRequest;

public record ErrorResponse(UserTypeRequest userType, String email, String error) {
}