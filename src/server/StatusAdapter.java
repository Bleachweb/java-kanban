package server;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import statuses.Status;

import java.io.IOException;

public class StatusAdapter extends TypeAdapter<Status> {
    @Override
    public void write(JsonWriter out, Status value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.name());
        }
    }

    @Override
    public Status read(JsonReader in) throws IOException {
        String statusString = in.nextString();
        if (statusString == null || statusString.equals("null")) {
            return null;
        }
        return Status.valueOf(statusString);
    }
}