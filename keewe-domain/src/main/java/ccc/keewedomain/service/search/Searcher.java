package ccc.keewedomain.service.search;

import ccc.keewedomain.persistence.repository.utils.CursorPageable;

import java.util.List;

public interface Searcher<T> {
    List<T> search(String keyword, CursorPageable<Long> cPage);
}
