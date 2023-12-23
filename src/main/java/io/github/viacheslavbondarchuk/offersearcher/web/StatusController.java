package io.github.viacheslavbondarchuk.offersearcher.web;

import io.github.viacheslavbondarchuk.offersearcher.service.StatusAwareService;
import io.github.viacheslavbondarchuk.offersearcher.util.KeyValuePair;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("status")
@SuppressWarnings({"rawtypes"})
public class StatusController {
    private final List<StatusAwareService> services;

    @GetMapping
    public List<KeyValuePair> getStatus() {
        return services.stream()
                .map(StatusAwareService::getStatus)
                .toList();
    }

}
