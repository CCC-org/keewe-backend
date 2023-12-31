package ccc.keewedomain.dto.insight;

import lombok.Getter;

@Getter
public class InsightWriterDto {
    private Long writerId;
    private String nickname;
    private String title;
    private String image;

    public InsightWriterDto(Long writerId, String nickname, String title, String image) {
        this.writerId = writerId;
        this.nickname = nickname;
        this.title = title;
        this.image = image;
    }

    public static InsightWriterDto of(Long writerId, String nickname, String title, String image) {
        return new InsightWriterDto(writerId, nickname, title, image);
    }
}
