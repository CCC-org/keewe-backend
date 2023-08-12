package ccc.keewecore.dto;


import ccc.keewecore.consts.TitleCategory;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TitleEvent implements Serializable {
    private TitleCategory category;
    private String name;
    private String introduction;
    private String createdTime;
}
