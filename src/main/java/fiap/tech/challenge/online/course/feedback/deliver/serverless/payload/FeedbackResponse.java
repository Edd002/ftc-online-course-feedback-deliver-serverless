package fiap.tech.challenge.online.course.feedback.deliver.serverless.payload;

public record FeedbackResponse(Boolean urgent, String description, String comment, String teacherName, String teacherEmail, String studentName, String studentEmail, String assessmentName, AssessmentType assessmentType, Double assessmentScore) {
}