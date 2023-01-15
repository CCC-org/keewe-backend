package ccc.keeweinfra.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attachments {
    private String color;
    private List<Fields> fields;

    public static Attachments of(String color, List<Fields> fields) {
        return new Attachments(color, fields);
    }
}
