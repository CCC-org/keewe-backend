package ccc.keewedomain.persistence.domain.title.enums;

public enum InsightTitle {
    게시글_최초(2000L, 1L),
    게시글_5개(2001L, 5L),
    게시글_10개(2002L, 10L),
    게시글_50개(2003L, 50L),
    게시글_100개(2004L, 100L),
    기준_미충족(-1L, -1L);

    public Long id;
    public Long standard;

    InsightTitle(Long id, Long standard) {
        this.id = id;
        this.standard = standard;
    }
}
