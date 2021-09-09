package goRest.Model;

import java.util.List;

public class CommentsBody {
    private Meta meta;
   private List<Data> data;

    @Override
    public String toString() {
        return "GoRestCommentsAll{" +
                "meta=" + meta +
                ", dataList=" + data +
                '}';
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
