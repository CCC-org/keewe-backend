package ccc.keeweapi.dto.insight.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class BookmarkToggleResponse {
    private boolean isBookmark;
}
