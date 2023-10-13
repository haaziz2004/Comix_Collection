public class Value implements comicValue{
    protected comicBook book;
    public Value(comicBook comic){
        this.book = comic;
    }
    @Override
    public double getValue() {
        return book.valueGet();
    }
}
