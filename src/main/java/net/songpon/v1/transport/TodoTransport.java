package net.songpon.v1.transport;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 */
public class TodoTransport {
    private String id;
    @NotNull(message = "title cannot be blank")
    @Size(max = 256, message = "title cannot be logger 256 char")
    private String title;
    @Size(max = 1000, message = "description cannot be logger 1000 char")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
