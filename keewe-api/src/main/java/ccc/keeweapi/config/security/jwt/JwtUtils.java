package ccc.keeweapi.config.security.jwt;

import ccc.keeweapi.exception.KeeweAuthException;
import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.KeeweRtnConsts;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${jwt.secret}")
    private String secretKey;

    // note. 약 41일
    private final long tokenValidTime = 600 * 600 * 10000L;

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(Long id, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(id));
        claims.put("roles", roles);

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader(KeeweConsts.AUTH_HEADER);

        if (StringUtils.hasText(header) && header.startsWith(KeeweConsts.BEARER)) {
            return header.substring(7);
        }

        return "";
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(this.getUserIdFromToken(token));

        return new UsernamePasswordAuthenticationToken(userDetails
                , ""
                , userDetails.getAuthorities());
    }

    public boolean validateTokenOrElseThrow(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }
        catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new KeeweAuthException(KeeweRtnConsts.ERR401);
        } catch (ExpiredJwtException ex) {
            throw ex;
        }

    }

    private String getUserIdFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody()
                .getSubject();
    }

}
