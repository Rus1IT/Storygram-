package root.storygram.webtoken;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import root.storygram.controller.ControllerAdvice;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ControllerAdvice controllerAdvice;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            String jwtToken = authHeader.replace("Bearer ", "");
            String username = jwtService.extractUsername(jwtToken);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (userDetails != null && jwtService.isTokenValid(jwtToken)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            ResponseEntity<?> responseEntity = controllerAdvice.buildErrorResponse("Token expired: " + e.getMessage(), "token", HttpStatus.UNAUTHORIZED);
            sendErrorResponse(response, responseEntity);
        } catch (MalformedJwtException e) {
            ResponseEntity<?> responseEntity = controllerAdvice.buildErrorResponse("Malformed token: " + e.getMessage(), "token", HttpStatus.UNAUTHORIZED);
            sendErrorResponse(response, responseEntity);
        } catch (UnsupportedJwtException e) {
            ResponseEntity<?> responseEntity = controllerAdvice.buildErrorResponse("Unsupported token: " + e.getMessage(), "token", HttpStatus.UNAUTHORIZED);
            sendErrorResponse(response, responseEntity);
        } catch (UsernameNotFoundException e) {
            ResponseEntity<?> responseEntity = controllerAdvice.buildErrorResponse("Username not found: " + e.getMessage(), "username", HttpStatus.NOT_FOUND);
            sendErrorResponse(response, responseEntity);
        } catch (Exception e) {
            ResponseEntity<?> responseEntity = controllerAdvice.buildErrorResponse("Bad credentials: " + e.getMessage(), "token", HttpStatus.UNAUTHORIZED);
            sendErrorResponse(response, responseEntity);
        }

    }

    private void sendErrorResponse(HttpServletResponse response, ResponseEntity<?> responseEntity) throws IOException {
        response.setStatus(responseEntity.getStatusCodeValue());
        responseEntity.getHeaders().forEach((headerName, headerValues) -> headerValues.forEach(value -> response.addHeader(headerName, value)));
        response.setContentType("application/json");
        String jsonBody = objectMapper.writeValueAsString(responseEntity.getBody());
        response.getWriter().write(jsonBody);
        response.getWriter().flush();
    }

}
