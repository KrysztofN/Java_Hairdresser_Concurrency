package org.example;

import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class AnimatedText {
    private Terminal terminal;

    public AnimatedText(Terminal terminal) {
        this.terminal = terminal;
    }

    public void animateText(String text) throws IOException, InterruptedException {
        for (int i = 0; i < text.length(); i++) {
            terminal.putCharacter(text.charAt(i));
            Thread.sleep(300);
        }
        terminal.putCharacter('\n');
        terminal.flush();
    }
}
