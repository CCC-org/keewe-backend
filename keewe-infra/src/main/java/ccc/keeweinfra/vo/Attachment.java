package ccc.keeweinfra.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {
    private String color;
    private List<Field> fields;

    public static Attachment of(String color, List<Field> fields) {
        return new Attachment(color, fields);
    }
}
