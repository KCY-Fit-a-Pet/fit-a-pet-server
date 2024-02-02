package com.kcy.fitapet.global.common.util.querydsl;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

public class RepositorySliceHelper {
    /**
     * List로 받은 contents를 Slice로 변환한다.
     * @param contents : 변환할 List
     * @param pageable : Pageable
     * @return Slice<T> : 변환된 Slice. 단, contents.size()가 pageable.getPageSize()보다 작을 경우 hasNext는 true이며, Slice의 size는 contents.size() - 1이다.
     */
    public static <T> Slice<T> toSlice(List<T> contents, Pageable pageable) {
        boolean hasNext = isContentSizeGreaterThanPageSize(contents, pageable);
        return new SliceImpl<>(hasNext ? subListLastContent(contents, pageable) : contents, pageable, hasNext);
    }

    private static <T> boolean isContentSizeGreaterThanPageSize(List<T> content, Pageable pageable) {
        return pageable.isPaged() && content.size() > pageable.getPageSize();
    }

    private static <T> List<T> subListLastContent(List<T> content, Pageable pageable) {
        return content.subList(0, pageable.getPageSize());
    }
}
