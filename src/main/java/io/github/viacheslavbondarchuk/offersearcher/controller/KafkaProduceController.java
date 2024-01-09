package io.github.viacheslavbondarchuk.offersearcher.controller;

import io.github.viacheslavbondarchuk.offersearcher.domain.KafkaProduceUpdateRequest;
import io.github.viacheslavbondarchuk.offersearcher.service.AuthorizationService;
import io.github.viacheslavbondarchuk.offersearcher.service.KafkaUpdateService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author: vbondarchuk
 * date: 1/8/2024
 * time: 1:40 PM
 **/

@RestController
@RequiredArgsConstructor
@RequestMapping("/kafka/produce")
public class KafkaProduceController implements WebController {
    private final AuthorizationService authorizationService;
    private final KafkaUpdateService kafkaUpdateService;

    @PostMapping(consumes = MimeTypeUtils.APPLICATION_JSON_VALUE, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ProducerRecord<String, String> update(@RequestBody @Validated KafkaProduceUpdateRequest kafkaProduceUpdateRequest,
                                                 HttpServletRequest request) {
        authorizationService.check(request);
        return kafkaUpdateService.produce(kafkaProduceUpdateRequest);
    }
}
