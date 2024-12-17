package com.ahimsarijalu.extrack.auth;

import com.ahimsarijalu.extrack.user.User;
import com.ahimsarijalu.extrack.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ahimsarijalu.extrack.user.UserUtils.mapUserToDTO;
import static com.ahimsarijalu.extrack.utils.MapperUtil.mapToApiResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterDTO registerDTO) throws Exception {
        authService.register(registerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToApiResponse(HttpStatus.CREATED.value(), true, "User registered successfully", null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginDTO loginDTO) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        var authUser = authenticationManager.authenticate(usernamePassword);
        var user = (User) authUser.getPrincipal();
        var token = tokenProvider.generateAccessToken(user);

        var jwt = new LoginResponse();
        jwt.setToken(token);
        jwt.setUser(mapUserToDTO(user));
        return ResponseEntity.ok(mapToApiResponse(HttpStatus.OK.value(), true, "User logged in successfully", jwt));
    }
}
