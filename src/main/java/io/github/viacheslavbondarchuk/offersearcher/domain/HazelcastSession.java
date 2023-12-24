package io.github.viacheslavbondarchuk.offersearcher.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class HazelcastSession implements Serializable {
    @Serial
    private static final long serialVersionUID = 3528456958224344451L;

    private final String ip;
    private final long issueAt;
    private final long expireAt;

}
