package ccc.keewedomain.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
@AllArgsConstructor(staticName = "of")
@Getter
public class Interest {

    @Column(name = "interest_name", nullable = false, length = 8)
    private String name;

    public static final List<Interest> defaultInterests = List.of(
                    "마케팅",
                    "기획",
                    "디자인",
                    "개발",
                    "창업",
                    "가상자산",
                    "주식",
                    "전시",
                    "영화",
                    "공연",
                    "독서",
                    "미술",
                    "사진",
                    "글쓰기",
                    "맛집",
                    "요리",
                    "뷰티",
                    "패션",
                    "여행",
                    "인테리어",
                    "일상",
                    "육아",
                    "반려동물",
                    "음악")
            .stream()
            .map(Interest::of)
            .collect(Collectors.toUnmodifiableList());

}
