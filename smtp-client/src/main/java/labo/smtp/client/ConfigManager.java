package labo.smtp.client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
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
        victims = loadEmailFromFile(victimsPath);
        validateVictims(victims);

        messages = loadMessagesFromFile(messagesPath);
        validateMessages(messages);
    }

    private void loadProperties(String path) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(path)) {
            properties.load(fis);
        }
        smtpServer = properties.getProperty("smtpServer");
        smtpPort = Integer.parseInt(properties.getProperty("smtpPort"));
        groupCount = Integer.parseInt(properties.getProperty("groupCount"));
    }

    private void validateMessages(List<String> messages) {
        if (messages.isEmpty()) {
            throw new IllegalArgumentException("Le fichier des messages est vide.");
        }
        for (String message : messages) {
            String[] parts = message.split("\n", 2);
            if (!parts[0].startsWith("Subject:") || parts.length < 2 || parts[1].trim().isEmpty()) {
                throw new IllegalArgumentException("Message invalide détecté : " + message);
            }
        }
    }

    private List<String> loadMessagesFromFile(String path) throws IOException {
        List<String> messages = new ArrayList<>();
        StringBuilder currentMessage = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("---")) {
                    messages.add(currentMessage.toString().trim());
                    currentMessage.setLength(0); // Reset the buffer
                } else {
                    currentMessage.append(line).append("\n");
                }
            }
            if (currentMessage.length() > 0) {
                messages.add(currentMessage.toString().trim());
            }
        }
        return messages;
    }

    private void validateVictims(List<String> victims) {
        if (victims.size() < 2) {
            throw new IllegalArgumentException("Le fichier des victimes doit contenir au moins deux adresses e-mail.");
        }
        for (String email : victims) {
            if (!email.contains("@")) {
                throw new IllegalArgumentException("Adresse e-mail invalide détectée : " + email);
            }
        }
    }

    private List<String> loadEmailFromFile(String path) throws IOException {
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
