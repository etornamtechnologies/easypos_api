package com.etxtechstack.api.easypos_application.utils;


import com.etxtechstack.api.easypos_application.configs.ConfigParams;
import com.etxtechstack.api.easypos_application.models.User;
import com.etxtechstack.api.easypos_application.repositories.UserRepository;
import com.etxtechstack.api.easypos_application.vo.GeneralResponse;
import com.etxtechstack.api.easypos_application.vo.ValidateTokenResponse;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtUtil {
    private static final long serialVersionUID = -2550185165626007488L;
    @Autowired
    private Logger logger;

    @Autowired
    private ConfigParams configParams;

    @Autowired
    private UserRepository userRepository;

    public JwtUtil() {
    }

    public JwtUtil(ConfigParams configParams){
        this.configParams = configParams;
    }

    public String GenerateUserToken(User u){
        Date currentDate = new Date();
        long currentTimeLong = currentDate.getTime();
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(System.currentTimeMillis() + configParams.getJwtTokenValidity());
        //our algorithm we are using
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
        //this is the secret key converting it to a byte
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(configParams.getJwtSecretKey());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        Map<String,Object> claims = new HashMap<>();
        claims.put("userId", u.getId());
        claims.put("phone", u.getPhone());
        claims.put("username", u.getUsername());

        return Jwts.builder()
                .setId(u.getPhone())
                .setIssuedAt(currentDate)
                .setSubject("User")
                .setIssuer(configParams.getJwtIssuer())
                .setExpiration(expiryDate)
                .signWith(signatureAlgorithm, configParams.getJwtSecretKey())
                .setHeaderParam("Username", u.getName())
                .setClaims(claims).compact();
    }

    public ValidateTokenResponse validateUserToken(String token) {
        logger.info("Lets Check Token Validity");
        Integer tokenBearerIdx = token.indexOf("Bearer ");
        String userIdStr = null;
        Integer userId = null;
        String message = null;
        Integer code = CommonResponse.ERROR_CODE;
        if(tokenBearerIdx.equals(-1)) {
            return new ValidateTokenResponse(code, "Invalid Token", userId);
        }
        String authToken = token.substring(6);
        try {
            Jwts.parser().setSigningKey(configParams.getJwtSecretKey()).parseClaimsJws(authToken);
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(configParams.getJwtSecretKey()))
                    .parseClaimsJws(authToken).getBody();
            logger.info("==================USER TOKEN IS VALID");
            userIdStr = String.valueOf(claims.get("userId"));
            userId = Integer.valueOf(userIdStr);
            Optional<User> authUser = userRepository.findUserById(userId);
            if(!authUser.isPresent()) {
                message = CommonResponse.INVALID_JWT_TOKEN;
            } else {
                if(authUser.get().getStatus().equals("N")) {
                    message = "User Not Active";
                } else {
                    message = CommonResponse.SUCCESS_MSG;
                    code = CommonResponse.SUCCESS_CODE;
                }
            }
            return new ValidateTokenResponse(CommonResponse.SUCCESS_CODE, CommonResponse.SUCCESS_MSG, userId);
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
            message = "Invalid Access Token";
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
            message = "Expired Access Token";
        } catch (UnsupportedJwtException ex) {
            message = "Unsupported Access Token";
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
            message = "Access Token Claims String Is Empty";
            return null;
        } catch (Exception e) {
            logger.error("Invalid Token");
            message = CommonResponse.INVALID_JWT_TOKEN;
        }
        return new ValidateTokenResponse(code, message, userId);
    }
}

