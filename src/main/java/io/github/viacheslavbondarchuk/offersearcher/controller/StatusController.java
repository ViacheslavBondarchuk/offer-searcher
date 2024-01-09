package io.github.viacheslavbondarchuk.offersearcher.controller;

import io.github.viacheslavbondarchuk.offersearcher.service.AuthorizationService;
import io.github.viacheslavbondarchuk.offersearcher.service.StatusAwareService;
import io.github.viacheslavbondarchuk.offersearcher.util.KeyValuePair;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static io.github.viacheslavbondarchuk.offersearcher.constants.Headers.SECRET_KEY;

@RestController
@RequiredArgsConstructor
@RequestMapping("status")
@SuppressWarnings({"rawtypes"})
public class StatusController implements WebController {
    private final List<StatusAwareService> services;
    private final AuthorizationService authorizationService;

    @GetMapping
    public List<KeyValuePair> getStatus(@RequestHeader(SECRET_KEY) String secretKey) {
        authorizationService.check(secretKey);
        return services.stream()
                .map(StatusAwareService::getStatus)
                .toList();
    }


}
