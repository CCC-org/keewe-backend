package ccc.keewecore.dto;


import ccc.keewecore.consts.TitleCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TitleEvent {
    private TitleCategory category;
    private String name;
    private String introduction;
    private LocalDateTime createdTime;
}
