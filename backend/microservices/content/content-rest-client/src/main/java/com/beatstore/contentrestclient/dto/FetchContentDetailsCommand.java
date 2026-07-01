package com.beatstore.contentrestclient.dto;

import lombok.Data;

import java.util.Set;

@Data
public class FetchContentDetailsCommand {
    private Set<String> contentHashes;

    public FetchContentDetailsCommand(Set<String> contentHashes) {
        this.contentHashes = contentHashes;
    }
}
