package ru.nir.config;

import io.jsonwebtoken.Claims;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.nir.model.InstaUserDetails;
import ru.nir.service.JwtTokenManager;
import ru.nir.service.UserService;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtConf jwtConfig;
    private final JwtTokenManager tokenProvider;
    private final UserService userService;

    public JwtTokenAuthenticationFilter(
        JwtConf jwtConfig,
        JwtTokenManager tokenProvider,
        UserService userService) {

        this.jwtConfig = jwtConfig;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {

        // получите хедеры аутентификации
        String header = request.getHeader(jwtConfig.getHeader());

        // проверить хедер и префикс
        if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
            chain.doFilter(request, response); //если не валидный, перейти к следующему
            return;
        }

        // Если токен не предоставлен - пользователь не будет аутентифицирован

        //  Если пользователь попытался получить доступ без токена доступа,
        //  то он не будет аутентифицирован и будет выдано исключение

        // получаем токен
        String token = header.replace(jwtConfig.getPrefix(), "");

        if (tokenProvider.validateToken(token)) {
            Claims claims = tokenProvider.getClaimsFromJWT(token);
            String username = claims.getSubject();

            UsernamePasswordAuthenticationToken auth =
                userService.findByUsername(username)
                    .map(InstaUserDetails::new)
                    .map(userDetails -> {
                        UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        return authentication;
                    })
                    .orElse(null);

            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            SecurityContextHolder.clearContext();
        }
        //переходим к следующему фильтру
        chain.doFilter(request, response);
    }


}
