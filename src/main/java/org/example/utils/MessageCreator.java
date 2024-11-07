package org.example.utils;

public class MessageCreator {
    public static void sendMessage(String message, MessageType type) {
        if (type.equals(MessageType.NEXT)) {
            System.out.println(message);
            return;
        }
        System.out.print(message);
    }
}
