package net.songpon.v1.transport;

import java.util.List;

/**
 *
 */
public class TodoResponseTransport {
    private int size;
    private int total;
    private List<TodoTransport> data;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<TodoTransport> getData() {
        return data;
    }

    public void setData(List<TodoTransport> data) {
        this.data = data;
    }
}
