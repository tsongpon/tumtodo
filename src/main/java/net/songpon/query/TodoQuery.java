package net.songpon.query;

/**
 *
 */
public class TodoQuery {
    private final int size;
    private final int start;

    private TodoQuery(QueryBuilder builder) {
        this.size = builder.size;
        this.start = builder.start;
    }

    public int getSize() {
        return size;
    }

    public int getStart() {
        return start;
    }

    public static class QueryBuilder {
        private int size;
        private int start;

        public QueryBuilder size(int size) {
            this.size = size;
            return this;
        }

        public QueryBuilder start(int start) {
            this.start = start;
            return this;
        }

        public TodoQuery build() {
            return new TodoQuery(this);
        }
    }
}
