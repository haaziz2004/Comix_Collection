package Comic ;

public class ComicValueDecorator extends BaseComicDecorator{
    public ComicValueDecorator(IComic wrapped){
        super(wrapped);
    }

    public double getValue() {
        return super.getValue();
    }
}
