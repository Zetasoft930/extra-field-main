package com.fieldright.fr.entity.notification;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Notification {

    private JsonArray toMore;
    private String to;
    private String title;
    private String body;

    public Notification to(String to) {
        this.to = to;
        return this;
    }

    public Notification toMore(List<String> to) {
        toMore = new JsonArray();
        for (String s : to) {
            toMore.add(s);
        }
        return this;
    }

    public Notification setTitle(String title) {
        this.title = title;
        return this;
    }

    public Notification setBody(String body) {
        this.body = body;
        return this;
    }

    public JsonObject build() {
        JsonObject body = new JsonObject();
        if (toMore == null)
            body.addProperty("to", this.to);
        else
            body.add("to", this.toMore);

        body.addProperty("title", this.title);
        body.addProperty("body", this.body);

        return body;
    }
}