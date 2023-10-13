public class Slabbed implements comicValue{
    protected comicBook book;
    public Slabbed(comicBook comic){
        this.book = comic;
    }
    @Override
    public double getValue() {
        return 0;
    }
}
