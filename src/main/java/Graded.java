public class Graded extends comicDecorator{
    protected comicBook book;
    public Graded(comicBook comic){
        this.book = comic;
    }
    @Override
    public double getValue() {
        double temp = super.getValue();
        return temp;
    }
}
