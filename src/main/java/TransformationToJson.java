import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class TransformationToJson {

    public static String transToJson(List<PageEntry> list) {
        if (list.isEmpty()) {
            return "Поиск по вашему слову не дал результата!";
        } else {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            return gson.toJson(list);
        }
    }
}
