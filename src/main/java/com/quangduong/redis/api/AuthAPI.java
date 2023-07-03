package com.quangduong.redis.api;

import com.quangduong.redis.dto.user.UserDTO;
import com.quangduong.redis.security.CustomUserDetails;
import com.quangduong.redis.service.IUserService;
import com.quangduong.redis.utils.JwtUtils;
import com.quangduong.redis.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthAPI {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("signup")
    public ResponseEntity<AuthResponse> signUp(@RequestBody @Valid UserDTO dto) {
        UserDTO user = userService.createUser(dto);
        return new ResponseEntity<>(new AuthResponse(dto.getUsername(), jwtUtils.generateToken(user.getUsername())), HttpStatus.CREATED);
    }

    @PostMapping("signin")
    public ResponseEntity<AuthResponse> signIn(@RequestBody @Valid UserDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = securityUtils.getUserDetails();
        return ResponseEntity.ok(new AuthResponse(dto.getUsername(), jwtUtils.generateToken(userDetails.getUsername())));
    }

    record AuthResponse(String username, String token) {}
}
