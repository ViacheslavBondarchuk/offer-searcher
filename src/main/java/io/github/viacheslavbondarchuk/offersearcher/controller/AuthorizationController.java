package io.github.viacheslavbondarchuk.offersearcher.controller;

import io.github.viacheslavbondarchuk.offersearcher.domain.AuthorizationResponse;
import io.github.viacheslavbondarchuk.offersearcher.service.AuthorizationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authorize")
public class AuthorizationController implements WebController {
    private final AuthorizationService authorizationService;

    @GetMapping
    public AuthorizationResponse authorize(HttpServletRequest request) {
        return AuthorizationResponse.of(authorizationService.authorize(request));
    }

}
