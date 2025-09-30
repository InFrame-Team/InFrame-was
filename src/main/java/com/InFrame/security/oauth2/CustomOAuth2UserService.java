package com.InFrame.security.oauth2;

import com.InFrame.domains.user.entity.Role;
import com.InFrame.domains.user.entity.User;
import com.InFrame.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String providerId = null;
        String email = null;
        String name = null;

        if (provider.equals("kakao")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            providerId = attributes.get("id").toString();
            email = (String) kakaoAccount.get("email");
            name = (String) profile.get("nickname");

        } else if (provider.equals("naver")) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");

            providerId = response.get("id").toString();
            email = response.get("email").toString();
            name = response.get("name").toString();
        }

        Optional<User> optionalUser = userRepository.findByProviderAndProviderId(provider, providerId);
        User user;

        String nickname = provider + "_" + providerId.substring(0, 6);

        while (userRepository.findByNickname(nickname).isPresent()) {
            nickname += (int)(Math.random() * 10);
        }

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            user = User.builder()
                    .email(email)
                    .password("")
                    .nickname(nickname)
                    .name(name)
                    .role(Role.USER)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(user);
        }

        return new CustomOAuth2User(user, attributes);
    }
}
