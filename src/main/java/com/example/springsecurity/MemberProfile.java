package com.example.springsecurity;

import lombok.Getter;


@Getter
public class MemberProfile {
    private final String oauthId;
    private final String userId;
    private final String nickname;
    private final String profileImageUrl;

    public MemberProfile(String oauthId, String nickname, String userId, String profileImageUrl) {
        this.oauthId = oauthId;
        this.nickname = nickname;
        this.userId = userId;
        this.profileImageUrl = profileImageUrl;
    }

    public Member toMember() {
        return new Member(oauthId, nickname, userId, profileImageUrl, Role.USER);
    }
}
