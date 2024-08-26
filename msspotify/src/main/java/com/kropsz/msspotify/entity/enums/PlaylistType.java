package com.kropsz.msspotify.entity.enums;

public enum PlaylistType {
    USER_TRACKS("userTracks"),
    SUGGESTION_TRACKS("suggestionTracks");

    private final String prefix;

    PlaylistType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
