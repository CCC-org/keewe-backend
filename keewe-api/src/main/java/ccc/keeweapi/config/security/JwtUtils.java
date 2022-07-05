package ccc.keeweapi.config.security;

import ccc.keeweapi.config.UserPrincipal;
import ccc.keeweapi.consts.KeeweConsts;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtUtils {
    // 환경 변수로 이동
    private String secretKey = "keewe";

    private long tokenValidTime = 600 * 600 * 1000L;

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String email, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
//        UserDetails userDetails =
//                userDetailsService.loadUserByUsername(this.getUserEmail(token));

        UserDetails userDetails = new UserPrincipal(null);

        return new UsernamePasswordAuthenticationToken(userDetails
                , ""
                , userDetails.getAuthorities());
    }

    public String getUserEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody()
                .getSubject();
    }


    public String resolveToken(HttpServletRequest request) {
        String header = request.getHeader(KeeweConsts.AUTH_HEADER);

        if (StringUtils.hasText(header) && header.startsWith(KeeweConsts.BEARER)) {
            return header.substring(7);
        }

        return "";
    }

    public boolean validateTokenOrElseThrow(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        }
        catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("TOKEN_INVALID_CREDENTIALS", ex);
        } catch (ExpiredJwtException ex) {
            throw ex;
        }

    }

}
