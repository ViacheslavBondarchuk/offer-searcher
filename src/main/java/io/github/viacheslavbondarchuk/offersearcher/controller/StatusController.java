package io.github.viacheslavbondarchuk.offersearcher.controller;

import io.github.viacheslavbondarchuk.offersearcher.service.AuthorizationService;
import io.github.viacheslavbondarchuk.offersearcher.service.StatusAwareService;
import io.github.viacheslavbondarchuk.offersearcher.util.KeyValuePair;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("status")
@SuppressWarnings({"rawtypes"})
public class StatusController implements WebController {
    private final List<StatusAwareService> services;
    private final AuthorizationService authorizationService;

    @GetMapping
    public List<KeyValuePair> getStatus(HttpServletRequest request) {
        authorizationService.check(request);
        return services.stream()
                .map(StatusAwareService::getStatus)
                .toList();
    }


}
