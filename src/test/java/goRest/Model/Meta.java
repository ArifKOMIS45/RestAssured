package goRest.Model;

public class Meta {
    private Pagination pagination;


    @Override
    public String toString() {
        return "Meta{" +
                "pagination=" + pagination +
                '}';
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
