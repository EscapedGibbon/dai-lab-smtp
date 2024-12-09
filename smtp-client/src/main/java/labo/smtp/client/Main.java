package labo.smtp.client;

import java.util.List;

public class Main {

  public static void main(String[] args) {

    if (args.length != 3) {
      System.err.println(
          "Mauvais nombre d'arguments. Usage: java Main <config_filepath> <victims_filepath> <messages_filepath>");
      System.exit(1);
    }
    try {
      ConfigManager config = new ConfigManager(args[0], args[1], args[2]);

      // Valide les fichiers
      if (config.getVictims().isEmpty()) {
        throw new IllegalArgumentException("Le fichier des victimes est vide ou invalide.");
      }
      if (config.getMessages().isEmpty()) {
        throw new IllegalArgumentException("Le fichier des messages est vide ou invalide.");
      }
      if (config.getVictims().size() < config.getGroupCount() * 2) {
        throw new IllegalArgumentException(
            "Pas assez de victimes pour former " + config.getGroupCount() + " groupes.");
      }

      SMTPClient smtpClient = new SMTPClient(config.getSmtpServer(), config.getSmtpPort());
      smtpClient.connect();

      Prank prank = new Prank(config.getVictims(), config.getMessages(), config.getGroupCount());
      List<Email> emails = prank.createPranks();

      for (Email email : emails) {
        smtpClient.sendEmail(email);
      }

      smtpClient.disconnect();
    } catch (Exception e) {
      System.err.println("Erreur : " + e.getMessage());
      e.printStackTrace();
    }
  }
}
