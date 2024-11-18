package labo.smtp.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Prank {
    private List<String> victims;
    private List<String> messages;
    private int groupCount;

    public Prank(List<String> victims, List<String> messages, int groupCount) {
        this.victims = victims;
        this.messages = messages;
        this.groupCount = groupCount;
    }

    public List<Email> createPranks() {
        List<Email> emails = new ArrayList<>();
        Collections.shuffle(victims); // Randomize victims

        for (int i = 0; i < groupCount; i++) {
            int groupSize = new Random().nextInt(4) + 2; // Groups of 2-5
            if (groupSize > victims.size()) groupSize = victims.size();

            List<String> group = new ArrayList<>(victims.subList(0, groupSize));
            victims.subList(0, groupSize).clear();

            String sender = group.get(0);
            List<String> recipients = group.subList(1, group.size());
            String message = messages.get(new Random().nextInt(messages.size()));

            String subject = message.split("\n")[0];
            String body = message.substring(subject.length()).trim();

            emails.add(new Email(sender, recipients, subject, body));
        }

        return emails;
    }
}
