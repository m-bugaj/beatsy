package com.beatstore.marketplaceservice.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashSet;


/**
 * Simple generic wrapper for collections used in API responses.
 *
 * <p>✅ Use cases and benefits:
 * <ul>
 *   <li>Ensures a consistent JSON response structure — always returns an object
 *       like <code>{ "content": [...] }</code> instead of a raw array <code>[]</code>.</li>
 *   <li>Makes frontend integration simpler — clients can always access <code>response.content</code>.</li>
 *   <li>Stabilizes API contracts between microservices — prevents schema drift when
 *       collections are serialized differently.</li>
 * </ul>
 *
 * ⚠️ Might be unnecessary for internal or low-level APIs where simple lists/sets
 * are sufficient.
 */


@Getter
@NoArgsConstructor
public class CollectionWrapper<T> {
    private Collection<T> content = new HashSet<>();

    public CollectionWrapper(T t) {
        this.content.add(t);
    }

    public CollectionWrapper(Collection<T> collection) {
        this.content = collection;
    }

    public static <T> CollectionWrapper<T> of(Collection<T> collection) {
        return new CollectionWrapper<>(collection);
    }
}
