package ccc.keeweinfra.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Field {
    private String title;
    private String value;

    public static Field of(String title, String value) {
        return new Field(title, value);
    }
}
