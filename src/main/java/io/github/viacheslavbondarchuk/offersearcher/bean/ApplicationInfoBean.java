package io.github.viacheslavbondarchuk.offersearcher.bean;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class ApplicationInfoBean {
    private final String brandId;

    public ApplicationInfoBean(@Value("${brand.id}") String brandId) {
        this.brandId = brandId;
    }
}
