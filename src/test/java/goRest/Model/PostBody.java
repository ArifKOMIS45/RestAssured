package goRest.Model;

import java.util.List;

public class PostBody {
    private Meta meta;
    private List<DataPost> data;

    @Override
    public String toString() {
        return "{" +
                "meta=" + meta +
                ", dataPost=" + data +
                '}';
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<DataPost> getData() {
        return data;
    }

    public void setData(List<DataPost> data) {
        this.data = data;
    }
}
