package com.comix.api.comixapi.requestbody;

public class CreateComicRequestBody {
    private String storyTitle;
    private String publisher;
    private String issueNumber;
    private String description;
    private String seriesTitle;
    private String volumeNumber;
    private String publicationDate;

    public CreateComicRequestBody() {
    }

    public CreateComicRequestBody(String storyTitle, String publisher, String issueNumber, String description,
            String seriesTitle, String volumeNumber, String publicationDate) {
        this.storyTitle = storyTitle;
        this.publisher = publisher;
        this.issueNumber = issueNumber;
        this.description = description;
        this.seriesTitle = seriesTitle;
        this.volumeNumber = volumeNumber;
        this.publicationDate = publicationDate;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(String issueNumber) {
        this.issueNumber = issueNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeriesTitle() {
        return seriesTitle;
    }

    public void setSeriesTitle(String seriesTitle) {
        this.seriesTitle = seriesTitle;
    }

    public String getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(String volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

}
