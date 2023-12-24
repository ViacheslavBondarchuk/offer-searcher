package io.github.viacheslavbondarchuk.offersearcher.util;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.util.Base64;

@Slf4j
public final class JKSUtil {
    private static final String JKS_KEY_STORE = "JKS";

    private JKSUtil() {}

    public static String load(String value, String name, char[] password, String path) {
        String filePath = path.concat("/").concat(name);
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            KeyStore keyStore = KeyStore.getInstance(JKS_KEY_STORE);
            byte[] bytes = Base64.getMimeDecoder().decode(value);
            keyStore.load(new ByteArrayInputStream(bytes), password);
            keyStore.store(outputStream, password);
        } catch (Exception e) {
            log.error("Can not load file: ", e);
        }
        return filePath;
    }

}
