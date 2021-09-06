package goRest;

public class Links {
    private String previous;
    private String current;
    private String next;

    @Override
    public String toString() {
        return "GoRestCommentsLinks{" +
                "previous='" + previous + '\'' +
                ", current='" + current + '\'' +
                ", next='" + next + '\'' +
                '}';
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
