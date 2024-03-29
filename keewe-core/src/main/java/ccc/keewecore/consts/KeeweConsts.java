package ccc.keewecore.consts;

public class KeeweConsts {
    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER = "Bearer";
    public static final String AUTH_CODE = "authorization_code";

    public static final String DEFAULT_ROUTING_KEY = "";
    public static final String INSIGHT_VIEW_EXCHANGE = "INSIGHT-VIEW-EXCHANGE";
    public static final String INSIGHT_VIEW_QUEUE = "INSIGHT-VIEW-QUEUE";
    public static final String INSIGHT_REACT_EXCHANGE = "INSIGHT-REACT-EXCHANGE";
    public static final String INSIGHT_REACT_QUEUE = "INSIGHT-REACT-QUEUE";
    public static final String FOLLOW_FROM_INSIGHT_EXCHANGE = "FOLLOW-FROM-INSIGHT-EXCHANGE";
    public static final String FOLLOW_FROM_INSIGHT_QUEUE = "FOLLOW-FROM-INSIGHT-QUEUE";
    public static final String PROFILE_VISIT_FROM_INSIGHT_EXCHANGE = "PROFILE-VISIT-FROM-INSIGHT-EXCHANGE";
    public static final String PROFILE_VISIT_FROM_INSIGHT_QUEUE = "PROFILE-VISIT-FROM-INSIGHT-QUEUE";
    public static final String PUSH_SEND_QUEUE = "PUSH-SEND-QUEUE";
    public static final String PUSH_SEND_EXCHANGE = "PUSH-SEND-EXCHANGE";

    public static final String INSIGHT_VIEW_HASH_KEY = "insight-view";
    public static final String REACTION_COUNT_HASH_KEY = "reaction-count";
    public static final String REACTION_COUNT_TITLE_HASH_KEY = "reaction-count-title";
    public static final String HASH_KEY_DELIMITER = ":";
    
    public static final String LONG_MAX_STRING = "9223372036854775807";

    public static final Long REPRESENTATIVE_COMMENT_REPLY_LIMIT = 2L;

    public static final int MY_PAGE_TITLE_LIMIT = 3;

    public static final String EVENT_CONNECTION_HANDSHAKE = "SUCCESS";
    public static final String TITLE_ACQUIREMENT_EXCHANGE = "TITLE-ACQUIREMENT-EXCHANGE";
    public static final String TITLE_STAT_QUEUE = "TITLE-STAT-QUEUE";

    public static final String REPORT_NOTI_HEADER = "[\uD83D\uDEA8 신고가 접수됐어요. \uD83D\uDEA8]\n\n";
    public static final String SLACK_REPORT_BOT_NAME = "키위봇";
    public static final String SLACK_REPORT_BOT_IMG = ":robot_face:";

    public static final Integer PROFILE_PHOTO_WIDTH = 450;
    public static final Integer PROFILE_PHOTO_HEIGHT = 450;

    public static final String PUSH_DEFAULT_TITLE = "Keewe";
}
