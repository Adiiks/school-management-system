package com.adrian.school.management.system.controller;

import com.adrian.school.management.system.dto.JwtRequest;
import com.adrian.school.management.system.service.JwtUserDetailsService;
import com.adrian.school.management.system.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authenticate")
@RequiredArgsConstructor
public class JwtAuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final JwtUserDetailsService userDetailsService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.email(), authenticationRequest.password());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.email());

        return jwtTokenUtil.generateToken(userDetails);
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
