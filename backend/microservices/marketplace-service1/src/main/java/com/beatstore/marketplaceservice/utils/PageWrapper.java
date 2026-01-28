package com.beatstore.marketplaceservice.utils;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * A generic wrapper for paginated responses.
 * <p>
 * This class encapsulates both the list of items returned from a paginated query
 * and additional metadata about the current page, total elements, and total pages.
 * </p>
 *
 * <h3>Benefits:</h3>
 * <ul>
 *   <li>📦 Provides a consistent API response format for all endpoints returning paginated data.</li>
 *   <li>🧭 Separates pagination metadata (<code>PageInfo</code>) from content to improve clarity and maintainability.</li>
 *   <li>🔁 Simplifies JSON serialization — clients receive both items and pagination details in one object.</li>
 *   <li>🤝 Useful for inter-service communication to avoid leaking Spring Data <code>Page</code> internals.</li>
 * </ul>
 *
 * <p><b>Example JSON structure:</b></p>
 * <pre>
 * {
 *   "content": [
 *     { "id": 1, "title": "Drill Beat" },
 *     { "id": 2, "title": "Trap Beat" }
 *   ],
 *   "pageInfo": {
 *     "totalElements": 100,
 *     "totalPages": 5,
 *     "size": 20,
 *     "number": 1,
 *     "first": false,
 *     "last": false,
 *     "numberOfElements": 20
 *   }
 * }
 * </pre>
 */


public class PageWrapper<T> {
    private List<T> content;
    private PageInfo pageInfo;

    public PageWrapper(Page<T> page) {
        this.content = page.getContent();
        this.pageInfo = new PageInfo(page);
    }

    @Getter
    private class PageInfo {
        private long totalElements;
        private long totalPages;
        private int size;
        private int number;
        private boolean first;
        private boolean last;
        private int numberOfElements;

        public PageInfo(Page<T> page) {
            this.totalElements = page.getTotalElements();
            this.totalPages = page.getTotalPages();
            this.size = page.getSize();
            this.number = page.getNumber();
            this.first = page.isFirst();
            this.last = page.isLast();
            this.numberOfElements = page.getNumberOfElements();
        }
    }

}
