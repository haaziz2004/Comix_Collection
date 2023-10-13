public class comicBook{
    private String publisher;
    private String seriesTitle;
    private int volumeNumber;
    private int issueNumber;
    private String publicationDate;
    private String creators;
    private String principleCharacters;
    private String description;
    private double value;

    public comicBook(String publisher, String seriesTitle, int volumeNumber, int issueNumber, String publicationDate) {
        this.publisher = publisher;
        this.seriesTitle = seriesTitle;
        this.volumeNumber = volumeNumber;
        this.issueNumber = issueNumber;
        this.publicationDate = publicationDate;
    }

    public comicBook(String publisher, String seriesTitle, int volumeNumber, int issueNumber, String publicationDate,
                     String creators, String principleCharacters, String description, double value) {
        this(publisher, seriesTitle, volumeNumber, issueNumber, publicationDate);
        this.creators = creators;
        this.principleCharacters = principleCharacters;
        this.description = description;
        this.value = value;
    }

    // Getters
    public String publisherGet() {
        return publisher;
    }

    public String seriesTitleGet() {
        return seriesTitle;
    }

    public int volumeNumberGet() {
        return volumeNumber;
    }

    public int issueNumberGet() {
        return issueNumber;
    }

    public String publicationDateGet() {
        return publicationDate;
    }

    public String creatorsGet() {
        return creators;
    }

    public String principleCharactersGet() {
        return principleCharacters;
    }

    public String descriptionGet() {
        return description;
    }

    public double valueGet() {
        return value;
    }

    // Setters
    public void publisherSet(String publisher) {
        this.publisher = publisher;
    }

    public void seriesTitleSet(String seriesTitle) {
        this.seriesTitle = seriesTitle;
    }

    public void volumeNumberSet(int volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public void issueNumberSet(int issueNumber) {
        this.issueNumber = issueNumber;
    }

    public void publicationDateSet(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void creatorsSet(String creators) {
        this.creators = creators;
    }

    public void principleCharactersSet(String principleCharacters) {
        this.principleCharacters = principleCharacters;
    }

    public void descriptionSet(String description) {
        this.description = description;
    }

    public void valueSet(Double value) {
        this.value = value;
    }
}