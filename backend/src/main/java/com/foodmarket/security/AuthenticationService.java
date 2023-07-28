package com.foodmarket.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${auth.cookie.hmac-key:secret-key}")
    private String secretKey;

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public UserDetails authenticate(UsernamePasswordAuthenticationToken token) {
        log.info("authenticate UsernamePasswordAuthenticationToken");
        String username = token.getName();
        String password = token.getCredentials().toString();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException(String.format("Incorrect credentials for user: %s", username));
        }
        return userDetails;
    }

    public UserDetails authenticate(PreAuthenticatedAuthenticationToken token) {
        log.info("authenticate PreAuthenticatedAuthenticationToken");
        String tokenValue = token.getPrincipal().toString();

        String username = StringUtils.substringBefore(tokenValue, "&");
        String hmac = StringUtils.substringAfter(tokenValue, "&");

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if (!hmac.equals(calculateHmac(userDetails))) {
            throw new BadCredentialsException(String.format("Incorrect cookie for user: %s", username));
        }
        return userDetails;
    }

    public String createToken(UserDetails userDetails) {
        return userDetails.getUsername() + "&" + calculateHmac(userDetails);
    }

    private String calculateHmac(UserDetails userDetails) {
        byte[] secretKeyBytes = Objects.requireNonNull(secretKey).getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes = (userDetails.getUsername() + userDetails.getPassword()).getBytes(StandardCharsets.UTF_8);
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, mac.getAlgorithm());
            mac.init(secretKeySpec);
            byte[] resultBytes = mac.doFinal(valueBytes);
            return Base64.encodeBase64String(resultBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

}
