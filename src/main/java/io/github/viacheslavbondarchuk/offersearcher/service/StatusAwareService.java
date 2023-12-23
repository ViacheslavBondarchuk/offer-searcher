package io.github.viacheslavbondarchuk.offersearcher.service;

import io.github.viacheslavbondarchuk.offersearcher.domain.ServiceStatus;
import io.github.viacheslavbondarchuk.offersearcher.util.KeyValuePair;

public interface StatusAwareService<T extends ServiceStatus> {

    KeyValuePair<String, T> getStatus();

    String getServiceName();

}
