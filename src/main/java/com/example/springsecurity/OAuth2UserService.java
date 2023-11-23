package com.example.springsecurity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);  //OAuth 서비스 (카카오 등)에서 가져온 유저 정보를 담음
        log.info("[AccessToken: {}]", userRequest.getAccessToken().getTokenValue());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();  //OAuth 서비스 이름(kakao 등)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();  //OAuth 로그인 시 키값. 카카오, 네이버, 구글 등 다르기 때문

        Map<String, Object> attributes = oAuth2User.getAttributes();  //OAuth2 서비스의 유저 정보들
        MemberProfile memberProfile = OAuthAttributes.extract(registrationId, attributes);
        Member member = saveOrUpdateUserProfile(memberProfile);

        return createDefaultOAuth2User(member, attributes, userNameAttributeName);
    }

    private Member saveOrUpdateUserProfile(MemberProfile memberProfile) {
        Member member = memberRepository.findByOauthId(memberProfile.getOauthId());
        if (member != null) {
            return member.update(memberProfile.getUserId(), memberProfile.getNickname()
                    , memberProfile.getProfileImageUrl());
        }
        return memberRepository.save(memberProfile.toMember());
    }

    private OAuth2User createDefaultOAuth2User(Member member
            , Map<String, Object> attributes, String userNameAttributeName) {
        return new DefaultOAuth2User(Collections.singletonList(new SimpleGrantedAuthority(
                member.getRole().getString())), attributes, userNameAttributeName);
    }
}
