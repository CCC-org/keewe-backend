package ccc.keeweapi.config;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
        String jwt = parseJwt(request);
        try {
            //TODO jwt가 없는경우 / 있는데 not valid한 경우
            if(!StringUtils.hasText(jwt))
                throw new BadCredentialsException("토큰이 없어용.");


            if(jwtUtils.validateTokenOrElseThrow(jwt)) {
                SecurityContextHolder.getContext().setAuthentication(jwtUtils.getAuthentication(jwt));
                chain.doFilter(request, response);
            }
            throw new BadCredentialsException("토큰이 이상해용.");

        } catch (BadCredentialsException ex) {
            request.setAttribute("exception", "STH");
            authenticationEntryPoint.commence(request, response, ex);

        } catch (ExpiredJwtException ex) {
            request.setAttribute("exception", "STH");
            authenticationEntryPoint.commence(request, response, new BadCredentialsException("토큰이 만료."));
        }


    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");//FIXME

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {//FIXME
            return headerAuth.substring(7);
        }

        return "";
    }


}
