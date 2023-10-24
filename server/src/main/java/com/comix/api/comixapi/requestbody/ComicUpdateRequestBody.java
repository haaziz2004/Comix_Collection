package com.comix.api.comixapi.requestbody;

public class ComicUpdateRequestBody {
    private String storyTitle;
    private String publisher;
    private String issueNumber;
    private String description;
    private String seriesTitle;
    private String volumeNumber;
    private String publicationDate;
    private String value;
    private String grade;
    private String slabbed;
    private String creators;
    private String principleCharacters;

    public ComicUpdateRequestBody() {
    }

    public ComicUpdateRequestBody(String storyTitle, String publisher, String issueNumber, String description,
            String seriesTitle, String volumeNumber, String publicationDate, String value, String grade,
            String slabbed, String creators, String principleCharacters) {
        this.storyTitle = storyTitle;
        this.publisher = publisher;
        this.issueNumber = issueNumber;
        this.description = description;
        this.seriesTitle = seriesTitle;
        this.volumeNumber = volumeNumber;
        this.publicationDate = publicationDate;
        this.value = value;
        this.grade = grade;
        this.slabbed = slabbed;
        this.creators = creators;
        this.principleCharacters = principleCharacters;
    }

    public String getCreators() {
        return creators;
    }

    public void setCreators(String creators) {
        this.creators = creators;
    }

    public String getPrincipleCharacters() {
        return principleCharacters;
    }

    public void setPrincipleCharacters(String principleCharacters) {
        this.principleCharacters = principleCharacters;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSlabbed() {
        return slabbed;
    }

    public void setSlabbed(String slabbed) {
        this.slabbed = slabbed;
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
