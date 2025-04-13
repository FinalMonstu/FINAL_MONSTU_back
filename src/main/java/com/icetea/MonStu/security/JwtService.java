package com.icetea.MonStu.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final String secretKey;    //Base64 인코딩된 256-bit 키 문자열

    public JwtService( @Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }


    public String extractUsername(String token) { return extractClaim(token, Claims::getSubject); }     // 토큰에서 username(=subject)을 추출

    /*
         토큰에서 특정 claim 추출 (예: username, expiration 등)
        claimsResolver : 어떤 클레임을 꺼낼 건지 함수형으로 전달
        사용 예시) String username = extractClaim(token, Claims::getSubject);
        추가한 claims 사용 예시) List<String> roles = extractClaim(token, claims -> claims.get("roles", List.class));
    */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);  // 모든 클레임 추출
        return claimsResolver.apply(claims);            // 원하는 클레임만 리턴
    }

    public String generateToken(UserDetails userDetails) {  // JWT 토큰 생성 (Role,nickname등도 추가 가능)
        return Jwts.builder()
                .setSubject(userDetails.getUsername())                         // 토큰의 주제(보통 username)
//                .claim("roles", userDetails.getAuthorities())                  // 사용자 권한 추가, 목록 타입도 가능
                .setIssuedAt(new Date(System.currentTimeMillis()))             // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12)) // 만료 시간 (24시간)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)              // HMAC-SHA256으로 서명
                .compact();                                                    // 최종 JWT 문자열로 압축
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {    // 토큰이 유효한지 확인 (username 일치 && 만료되지 않았는지)
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public void setOption(HttpServletResponse response,String jwtToken){
        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setHttpOnly(true); // 자바스크립트에서 접근 불가능
        cookie.setSecure(true);   // HTTPS 환경에서만 전송 (개발 중에는 false로 설정 가능)
//        cookie.setAttribute("SameSite", "Strict"); // 혹은 "Lax" 옵션 사용
        cookie.setPath("/");      // 도메인 내 모든 경로에서 사용 가능
        cookie.setMaxAge(24 * 60 * 60); // 1일 (초 단위)
        response.addCookie(cookie);
    }

    // 토큰 만료 여부 확인
    private boolean isTokenExpired(String token) { return extractExpiration(token).before(new Date()); }

    // 토큰에서 만료일자 추출
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 토큰 전체 Claims 추출 (내용 모두 보기)
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())          // 서명 키 설정
                .build()
                .parseClaimsJws(token)                // 토큰 파싱
                .getBody();                           // Claims (username, expiration 등) 리턴
    }

    // 서명용 Secret Key 생성 (Base64로 디코딩된 byte → Key 객체로 변환)
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);     // Base64 디코딩
        return Keys.hmacShaKeyFor(keyBytes);                      // HMAC-SHA용 키 생성
    }
}

