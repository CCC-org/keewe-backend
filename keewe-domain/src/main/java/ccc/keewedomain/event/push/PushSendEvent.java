package ccc.keewedomain.event.push;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
public class PushSendEvent implements Serializable {
    private Long userId;
    private String title;
    private String content;
}
