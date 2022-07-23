package ccc.keewedomain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotePostDto {
    private List<String> candidates;
    private Long profileId;
    private Long userId;
    private String contents;
}
