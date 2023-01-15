package ccc.keeweinfra.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Fields {
    private String title;
    private String value;

    public static Fields of(String title, String value) {
        return new Fields(title, value);
    }
}
