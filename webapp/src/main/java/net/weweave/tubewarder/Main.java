package net.weweave.tubewarder;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        Config config = createConfig(args);
        config.checkConfig();
        config.printConfig();
        TubewarderSwarm swarm = new TubewarderSwarm(config);
        swarm.deployTubewarder();
    }

    private static Config createConfig(String[] args) throws IOException {
        String configPath = (args.length >= 1 ? args[0] : "./tubewarder.conf");
        File configFile = new File(configPath);
        if (configFile.exists() && configFile.canRead()) {
            return new Config(configFile);
        } else {
            throw new IOException("Cannot read config file: " + configPath);
        }
    }
}
