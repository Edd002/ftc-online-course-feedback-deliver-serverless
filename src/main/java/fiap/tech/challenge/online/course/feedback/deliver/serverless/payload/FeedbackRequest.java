package fiap.tech.challenge.online.course.feedback.deliver.serverless.payload;

public record FeedbackRequest(UserTypeRequest userTypeRequest, String email, String accessKey, Boolean urgent, String description, String comment) {

    public String getEmail() {
        return this.email;
    }

    public String getAccessKey() {
        return this.accessKey;
    }
}