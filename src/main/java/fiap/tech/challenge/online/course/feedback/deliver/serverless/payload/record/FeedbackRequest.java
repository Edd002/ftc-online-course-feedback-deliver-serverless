package fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.record;

import fiap.tech.challenge.online.course.feedback.deliver.serverless.payload.enumeration.UserType;

public record FeedbackRequest(UserType userType, String email, String accessKey, Boolean urgent, String description, String comment) {
}