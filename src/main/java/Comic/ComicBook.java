package Comic ;

public class ComicBook implements IComic{
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

    public double getValue() {
        return 0;
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
}