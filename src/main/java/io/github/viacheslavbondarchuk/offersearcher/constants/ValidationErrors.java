package io.github.viacheslavbondarchuk.offersearcher.constants;

/**
 * author: vbondarchuk
 * date: 1/8/2024
 * time: 4:15 PM
 **/
public interface ValidationErrors {
    String ENTITY_TYPE_VALIDATION_ERROR = "Must not be null. Available types: [EVENT, MARKET, SELECTION]";
    String OFFER_TYPE_VALIDATION_ERROR = "Must not be null. Available types: [ACTUAL, UPDATES]";

}
