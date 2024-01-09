package io.github.viacheslavbondarchuk.offersearcher.controller;

import io.github.viacheslavbondarchuk.offersearcher.domain.KafkaProduceUpdateRequest;
import io.github.viacheslavbondarchuk.offersearcher.service.AuthorizationService;
import io.github.viacheslavbondarchuk.offersearcher.service.KafkaUpdateService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.github.viacheslavbondarchuk.offersearcher.constants.Headers.SECRET_KEY;

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
                                                 @RequestHeader(SECRET_KEY) String secretKey) {
        authorizationService.check(secretKey);
        return kafkaUpdateService.produce(kafkaProduceUpdateRequest);
    }
}
