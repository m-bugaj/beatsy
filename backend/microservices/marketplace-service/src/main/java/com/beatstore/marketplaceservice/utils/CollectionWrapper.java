package com.beatstore.marketplaceservice.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashSet;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CollectionWrapper<T> {
    private Collection<T> content = new HashSet<>();

    public CollectionWrapper(T t) {
        this.content.add(t);
    }

    public static <T> CollectionWrapper<T> of(Collection<T> collection) {
        return new CollectionWrapper<>(collection);
    }
}
