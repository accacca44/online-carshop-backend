package edu.bbte.idde.keim2152.spring.controller;

import edu.bbte.idde.keim2152.spring.auth.payload.IntrospectResponse;
import edu.bbte.idde.keim2152.spring.auth.payload.JwtResponse;
import edu.bbte.idde.keim2152.spring.auth.payload.LoginRequest;
import edu.bbte.idde.keim2152.spring.auth.payload.RegisterRequest;
import edu.bbte.idde.keim2152.spring.auth.payload.jwt.JwtManager;
import edu.bbte.idde.keim2152.spring.mapper.UserMapper;
import edu.bbte.idde.keim2152.spring.model.domain.User;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.ResponseMessageDto;
import edu.bbte.idde.keim2152.spring.repository.impl.jpa.JpaUserDao;
import edu.bbte.idde.keim2152.spring.service.UserDetailsImp;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Value("${app.jwtExpiration}")
    private Integer jwtExpiration;

    private final UserMapper userMapper;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JpaUserDao userDao;

    @Autowired
    JwtManager jwtManager;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("introspect")
    public ResponseEntity<?> introspect(HttpServletRequest request, HttpServletResponse response) {
        
        // Request the logged-in users information (the same as in the token)
        String jwt = jwtManager.getJwtFromCookie(request);
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseMessageDto("User Unauthorized!"));
        }

        // If the user has a token return user details
        try {
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication auth = context.getAuthentication();
            UserDetailsImp user = (UserDetailsImp) auth.getPrincipal();
            String userName = user.getUsername();
            Long userId = user.getId();
            String role = String.valueOf(user.getAuthorities().stream().findFirst().get());
            User userDummy = userDao.findById(user.getId()).get();
            String lang = userDummy.getLang();
            String theme = userDummy.getTheme();

            return ResponseEntity.ok().body(new IntrospectResponse(userName, userId, role, lang, theme));
        } catch (Error e) {
            log.error(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseMessageDto("User Unauthorized!"));
        }
    }

    @PostMapping("login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtManager.generateJwtToken(authentication);
        log.info("JWT Generated: " + jwt);

        UserDetailsImp userDetails = (UserDetailsImp) authentication.getPrincipal();
        User user = userDao.findById(userDetails.getId()).get();
        String lang = user.getLang();
        String theme = user.getTheme();

        Cookie cookie = new Cookie("token", jwt);
        cookie.setPath("/");
        cookie.setMaxAge(jwtExpiration / 1000);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setAttribute("SameSite", "Strict");

        // Find role of the user
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        response.addCookie(cookie);
        log.info(theme + "******************");
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles.get(0),
                lang,
                theme));
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userDao.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ResponseMessageDto("Error: Username is already taken!"));
        }

        if (userDao.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ResponseMessageDto("Error: Email is already in use!"));
        }

        // Encode the password
        String encodedPass = encoder.encode(registerRequest.getPassword());
        registerRequest.setPassword(encodedPass);

        // Create new user's account
        User user = new User();
        userMapper.registerToDomain(registerRequest, user);
        user.setLang("en");
        user.setTheme("nature");
        userDao.save(user);
        log.info("User registered successfully!");

        return ResponseEntity.ok(new ResponseMessageDto("User registered successfully!"));
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        response.addCookie(getInvalidatedCookie());
        return ResponseEntity.ok(new ResponseMessageDto("User logged out successfully!"));
    }

    private Cookie getInvalidatedCookie() {
        Cookie cookie;
        cookie = new Cookie("token", "0");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }

}
