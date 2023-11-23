package com.example.springsecurity;


import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;


public enum OAuthAttributes {
    KAKAO("kakao", attributes -> {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        return new MemberProfile(
                String.valueOf(attributes.get("id")),
                (String) profile.get("nickname"),
                (String) kakaoAccount.get("email"),
                (String) profile.get("profile_image_url")
        );
    });

    private final String registrationId;
    private final Function<Map<String, Object>, MemberProfile> userProfileFactory;

    OAuthAttributes(String registrationId,
                    Function<Map<String, Object>, MemberProfile> userProfileFactory) {
        this.registrationId = registrationId;
        this.userProfileFactory = userProfileFactory;
    }

    public static MemberProfile extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> registrationId.equals(provider.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .userProfileFactory.apply(attributes);
    }
}
