package Comic;

public class ComicBook implements IComic {
    private String publisher;
    private String seriesTitle;
    private int volumeNumber;
    private int issueNumber;
    private String publicationDate;
    private String creators;
    private String principleCharacters;
    private String description;

    public ComicBook(String publisher, String seriesTitle, int volumeNumber, int issueNumber, String publicationDate) {
        this.publisher = publisher;
        this.seriesTitle = seriesTitle;
        this.volumeNumber = volumeNumber;
        this.issueNumber = issueNumber;
        this.publicationDate = publicationDate;
    }

    public ComicBook(String publisher, String seriesTitle, int volumeNumber, int issueNumber, String publicationDate,
            String creators, String principleCharacters, String description) {
        this(publisher, seriesTitle, volumeNumber, issueNumber, publicationDate);
        this.creators = creators;
        this.principleCharacters = principleCharacters;
        this.description = description;
    }

    // Getters
    public String getPublisher() {
        return publisher;
    }

    public String getSeriesTitle() {
        return seriesTitle;
    }

    public int getVolumeNumber() {
        return volumeNumber;
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public String getCreators() {
        return creators;
    }

    public String getPrincipleCharacters() {
        return principleCharacters;
    }

    public String getDescription() {
        return description;
    }

    public double getValue() {
        return 0;
    }

    // Setters
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setSeriesTitle(String seriesTitle) {
        this.seriesTitle = seriesTitle;
    }

    public void setVolumeNumber(int volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public void setIssueNumber(int issueNumber) {
        this.issueNumber = issueNumber;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setCreators(String creators) {
        this.creators = creators;
    }

    public void setPrincipleCharacters(String principleCharacters) {
        this.principleCharacters = principleCharacters;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}