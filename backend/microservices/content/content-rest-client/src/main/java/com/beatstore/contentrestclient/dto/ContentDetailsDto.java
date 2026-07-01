package com.beatstore.contentrestclient.dto;

import com.beatstore.contentrestclient.common.enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentDetailsDto {
    private String contentHash;
    private String contentName;
    private ContentType contentType;
}
