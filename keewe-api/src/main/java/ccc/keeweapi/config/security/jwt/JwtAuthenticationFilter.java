package ccc.keeweapi.config.security.jwt;

import ccc.keeweapi.exception.KeeweAuthException;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private final JwtUtils jwtUtils;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager
            , AuthenticationEntryPoint authenticationEntryPoint
            , JwtUtils tokenProvider) {

        super(authenticationManager, authenticationEntryPoint);
        this.jwtUtils = tokenProvider;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = jwtUtils.extractToken(request);
        try {
            if(!StringUtils.hasText(jwt)) {
                throw new KeeweAuthException(KeeweRtnConsts.ERR403);
            }
            if (jwtUtils.validateTokenOrElseThrow(jwt)) {
                SecurityContextHolder.getContext().setAuthentication(jwtUtils.getAuthentication(jwt));
                chain.doFilter(request, response);
            } else {
                throw new KeeweAuthException(KeeweRtnConsts.ERR401);
            }
        } catch (KeeweException ex) {
            authenticationEntryPoint.commence(request, response, new KeeweAuthException(ex.getKeeweRtnConsts()));
        } catch (KeeweAuthException ex) {
            authenticationEntryPoint.commence(request, response, ex);
        } catch (ExpiredJwtException ex) {
            authenticationEntryPoint.commence(request, response, new KeeweAuthException(KeeweRtnConsts.ERR402));
        }
    }
}
