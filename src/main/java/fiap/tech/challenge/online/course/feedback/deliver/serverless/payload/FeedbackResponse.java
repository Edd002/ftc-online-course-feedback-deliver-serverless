package fiap.tech.challenge.online.course.feedback.deliver.serverless.payload;

public record FeedbackResponse(Boolean urgent, String description, String comment, String studentName, String studentEmail) {
}