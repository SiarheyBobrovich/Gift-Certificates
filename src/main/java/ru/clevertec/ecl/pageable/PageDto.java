package ru.clevertec.ecl.pageable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@JsonIgnoreProperties({"pageable", "sort"})
public class PageDto<T> extends PageImpl<T> {

    public PageDto(Page<T> page) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
    }

    public PageDto(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    @Override
    public int getTotalPages() {
        return super.getTotalPages();
    }

    @Override
    public int getNumber() {
        return super.getNumber();
    }

    @Override
    public int getSize() {
        return super.getSize();
    }


    @Override
    public boolean hasNext() {
        return super.hasNext();
    }

    @Override
    public boolean isFirst() {
        return super.isFirst();
    }

    @Override
    public boolean isLast() {
        return super.isLast();
    }

    @Override
    public boolean hasPrevious() {
        return super.hasPrevious();
    }

    @Override
    @NonNull
    public List<T> getContent() {
        return super.getContent();
    }

    @Override
    @NonNull
    public <U> Page<U> map(@NonNull Function<? super T, ? extends U> converter) {
        List<U> list = getContent().stream().map(converter).collect(Collectors.toList());
        return new PageDto<>(list, getPageable(), getTotalElements());
    }

    public static <T> PageDto<T> of(Page<T> page) {
        return new PageDto<>(page);
    }
}
