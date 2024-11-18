package labo.smtp.client;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigManager {
    private List<String> victims;
    private List<String> messages;
    private int groupCount;
    private String smtpServer;
    private int smtpPort;

    public ConfigManager(String configPath, String victimsPath, String messagesPath) throws IOException {
        loadProperties(configPath);
        victims = loadListFromFile(victimsPath);
        messages = loadListFromFile(messagesPath);
    }

    private void loadProperties(String path) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(path));
        smtpServer = properties.getProperty("smtpServer");
        smtpPort = Integer.parseInt(properties.getProperty("smtpPort"));
        groupCount = Integer.parseInt(properties.getProperty("groupCount"));
    }

    private List<String> loadListFromFile(String path) throws IOException {
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("@")) {
                    list.add(line.trim());
                } else {
                    System.err.println("Invalid email: " + line);
                }
            }
        }
        return list;
    }

    public List<String> getVictims() {
        return victims;
    }

    public List<String> getMessages() {
        return messages;
    }

    public int getGroupCount() {
        return groupCount;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public int getSmtpPort() {
        return smtpPort;
    }
}
