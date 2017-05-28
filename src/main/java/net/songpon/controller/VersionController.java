package net.songpon.controller;

import net.songpon.exception.TumtodoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 */

@RestController
@RequestMapping("/api/versions")
public class VersionController {

    private final Logger LOGGER = LoggerFactory.getLogger(VersionController.class);

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getVersions() {
        try {
            File file = ResourceUtils.getFile("classpath:apiVersions.json");
            String content = new String(Files.readAllBytes(file.toPath()));
            return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(content);
        } catch (IOException e) {
            LOGGER.error("error while reading version info", e);
            throw new TumtodoException("error while read version info", e);
        }
    }
}
