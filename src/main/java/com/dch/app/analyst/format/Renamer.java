package com.dch.app.analyst.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by ִלטענטי on 14.06.2015.
 */
public class Renamer {

    private static Logger logger = LoggerFactory.getLogger(Renamer.class);

    private String name;

    public Renamer(String n) {
        name = n;
    }

    public void rename(int count) throws IOException {
        Path p = Files.move(Paths.get(name),
                Paths.get(name + "-" + count + ".html"));
        logger.debug("result saved to \"{}\"", p.toAbsolutePath());
    }
}
