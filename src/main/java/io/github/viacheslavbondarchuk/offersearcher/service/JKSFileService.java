package io.github.viacheslavbondarchuk.offersearcher.service;

import io.github.viacheslavbondarchuk.offersearcher.domain.JKSFileType;
import io.github.viacheslavbondarchuk.offersearcher.util.JKSUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public final class JKSFileService {
    private final Map<JKSFileType, String> jksFilePathMap;

    public JKSFileService(Environment environment, @Value("${config.path}") String configPath) {
        this.jksFilePathMap = Arrays.stream(JKSFileType.values())
                .collect(Collectors.toMap(Function.identity(), type -> JKSUtil.load(
                        environment.getProperty(type.getValue()),
                        type.getName(),
                        environment.getProperty(type.getPassword()).toCharArray(),
                        configPath)));
    }

    public String getFilePath(JKSFileType type) {
        return jksFilePathMap.get(type);
    }
}
