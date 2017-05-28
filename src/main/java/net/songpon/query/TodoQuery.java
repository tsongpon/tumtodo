package net.songpon.query;

/**
 *
 */
public class TodoQuery {
    private final int size;
    private final int start;
    private final String title;

    private TodoQuery(QueryBuilder builder) {
        this.size = builder.size;
        this.start = builder.start;
        this.title = builder.title;
    }

    public int getSize() {
        return size;
    }

    public int getStart() {
        return start;
    }

    public String getTitle() {
        return title;
    }

    public static class QueryBuilder {
        private int size;
        private int start;
        private String title;

        public QueryBuilder size(int size) {
            this.size = size;
            return this;
        }

        public QueryBuilder start(int start) {
            this.start = start;
            return this;
        }

        public QueryBuilder title(String title) {
            this.title = title;
            return this;
        }

        public TodoQuery build() {
            return new TodoQuery(this);
        }
    }
}
