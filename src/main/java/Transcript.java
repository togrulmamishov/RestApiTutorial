public class Transcript {

    private String audio_url;
    private String id;
    private String status;
    private String text;
    private Boolean language_detection;

    public Boolean getLanguage_detection() {
        return language_detection;
    }

    public void setLanguage_detection(Boolean language_detection) {
        this.language_detection = language_detection;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
