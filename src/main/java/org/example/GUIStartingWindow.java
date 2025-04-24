package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GUIStartingWindow {
    private Screen screen;
    private Terminal terminal;
    private Map<String, String> configValues = new HashMap<>();
    private String[] fieldNames = {"Fryzjerzy S", "Fryzjerzy M", "Fryzjerzy G", "Liczba Foteli (LF)", "Wielkość Kolejki (WK)"};
    private String[] fieldKeys = {"F_S", "F_M", "F_G", "LF", "WK"};
    private boolean configConfirmed = false;
    private int selectedField = 0;

    public GUIStartingWindow() throws IOException {
        loadConfigFromFile();

        terminal = new DefaultTerminalFactory().createTerminal();
        screen = new TerminalScreen(terminal);
        screen.startScreen();
        screen.setCursorPosition(null);
        screen.clear();

        drawRandomPixels();
        showConfigScreen();
    }

    public void drawRandomPixels() {
        synchronized (screen) {
            try {
                screen.clear();

                Random random = new Random();
                TerminalSize terminalSize = screen.getTerminalSize();
                for(int column = 0; column < terminalSize.getColumns(); column++) {
                    for(int row = 0; row < terminalSize.getRows(); row++) {
                        screen.setCharacter(column, row, new TextCharacter(
                                ' ',
                                TextColor.ANSI.DEFAULT,
                                TextColor.ANSI.values()[random.nextInt(TextColor.ANSI.values().length)]));
                    }
                }

                String sizeLabel = "Witaj w salonie fryzjerskim";
                int centerX = terminalSize.getColumns() / 2;
                int centerY = terminalSize.getRows() / 2;
                TerminalPosition labelBoxTopLeft = new TerminalPosition(centerX - sizeLabel.length()/2, centerY);
                TerminalSize labelBoxSize = new TerminalSize(sizeLabel.length() + 2, 3);
                TerminalPosition labelBoxTopRightCorner = labelBoxTopLeft.withRelativeColumn(labelBoxSize.getColumns() - 1);
                TextGraphics textGraphics = screen.newTextGraphics();
                textGraphics.fillRectangle(labelBoxTopLeft, labelBoxSize, ' ');

                textGraphics.drawLine(
                        labelBoxTopLeft.withRelativeColumn(1),
                        labelBoxTopLeft.withRelativeColumn(labelBoxSize.getColumns() - 2),
                        Symbols.DOUBLE_LINE_HORIZONTAL);
                textGraphics.drawLine(
                        labelBoxTopLeft.withRelativeRow(2).withRelativeColumn(1),
                        labelBoxTopLeft.withRelativeRow(2).withRelativeColumn(labelBoxSize.getColumns() - 2),
                        Symbols.DOUBLE_LINE_HORIZONTAL);

                textGraphics.setCharacter(labelBoxTopLeft, Symbols.DOUBLE_LINE_TOP_LEFT_CORNER);
                textGraphics.setCharacter(labelBoxTopLeft.withRelativeRow(1), Symbols.DOUBLE_LINE_VERTICAL);
                textGraphics.setCharacter(labelBoxTopLeft.withRelativeRow(2), Symbols.DOUBLE_LINE_BOTTOM_LEFT_CORNER);
                textGraphics.setCharacter(labelBoxTopRightCorner, Symbols.DOUBLE_LINE_TOP_RIGHT_CORNER);
                textGraphics.setCharacter(labelBoxTopRightCorner.withRelativeRow(1), Symbols.DOUBLE_LINE_VERTICAL);
                textGraphics.setCharacter(labelBoxTopRightCorner.withRelativeRow(2), Symbols.DOUBLE_LINE_BOTTOM_RIGHT_CORNER);
                textGraphics.putString(labelBoxTopLeft.withRelative(1, 1), sizeLabel);

                screen.refresh();
                Thread.sleep(1500);
                screen.clear();

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadConfigFromFile() throws IOException {
        byte[] jsonData = Files.readAllBytes(Paths.get("src/main/java/org/example/config.json"));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(jsonData);

        configValues.put("F_S", String.valueOf(node.get("F").get("S").asInt()));
        configValues.put("F_M", String.valueOf(node.get("F").get("M").asInt()));
        configValues.put("F_G", String.valueOf(node.get("F").get("G").asInt()));
        configValues.put("LF", String.valueOf(node.get("LF").asInt()));
        configValues.put("WK", String.valueOf(node.get("WK").asInt()));
    }

    private void showConfigScreen() throws IOException {
        while (!configConfirmed) {
            screen.clear();
            TerminalSize size = screen.getTerminalSize();

            int startX = size.getColumns() / 4;
            int startY = size.getRows() / 6;

            // Rysowanie ramki
            TextGraphics textGraphics = screen.newTextGraphics();
            textGraphics.setForegroundColor(TextColor.ANSI.CYAN);

            String title = "Konfiguracja Salonu Fryzjerskiego";
            textGraphics.putString(startX + (size.getColumns() / 4) - (title.length() / 2), startY - 2, title);

            textGraphics.drawRectangle(
                    new TerminalPosition(startX - 2, startY - 3),
                    new TerminalSize(size.getColumns() / 2, fieldNames.length * 3 + 6),
                    Symbols.BLOCK_MIDDLE);

            // Wyświetlanie pól konfiguracyjnych
            for (int i = 0; i < fieldNames.length; i++) {
                int fieldY = startY + i * 3;
                textGraphics.setForegroundColor(i == selectedField ? TextColor.ANSI.YELLOW : TextColor.ANSI.WHITE);

                textGraphics.putString(startX, fieldY, fieldNames[i] + ":");

                // Rysowanie pola tekstowego
                textGraphics.drawRectangle(
                        new TerminalPosition(startX + 22, fieldY),
                        new TerminalSize(15, 1),
                        i == selectedField ? Symbols.BLOCK_SOLID : ' ');

                textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
                textGraphics.putString(startX + 23, fieldY, configValues.get(fieldKeys[i]));
            }

            // Instrukcje
            textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
            int instructionsY = startY + fieldNames.length * 3 + 2;
            textGraphics.putString(startX-2, instructionsY, " Nawigacja: ↑↓ | Edycja: Enter | Uruchom: F10 | Anuluj: Esc ");

            screen.refresh();

            KeyStroke keyStroke = terminal.readInput();

            if (keyStroke.getKeyType() == KeyType.ArrowDown) {
                selectedField = (selectedField + 1) % fieldNames.length;
            } else if (keyStroke.getKeyType() == KeyType.ArrowUp) {
                selectedField = (selectedField - 1 + fieldNames.length) % fieldNames.length;
            } else if (keyStroke.getKeyType() == KeyType.Enter) {
                // Edycja wybranego pola
                editField(startX + 22, startY + selectedField * 3);
            } else if (keyStroke.getKeyType() == KeyType.F10) {
                saveConfig();
                configConfirmed = true;
            } else if (keyStroke.getKeyType() == KeyType.Escape) {
                // Zamknij okno bez zmian
                screen.close();
                terminal.close();
                System.exit(0);
            }
            screen.setCursorPosition(null);

        }

        screen.close();
        terminal.close();
    }

    private void editField(int x, int y) throws IOException {
        StringBuilder fieldValue = new StringBuilder(configValues.get(fieldKeys[selectedField]));
        int cursorPosition = fieldValue.length();

        screen.setCursorPosition(new TerminalPosition(x + cursorPosition, y));

        boolean editing = true;
        while (editing) {
            KeyStroke keyStroke = terminal.readInput();

            if (keyStroke.getKeyType() == KeyType.Character) {
                if (cursorPosition < 10) { // Maksymalna długość pola
                    fieldValue.insert(cursorPosition, keyStroke.getCharacter());
                    cursorPosition++;
                }
            } else if (keyStroke.getKeyType() == KeyType.Backspace && cursorPosition > 0) {
                fieldValue.deleteCharAt(--cursorPosition);
            } else if (keyStroke.getKeyType() == KeyType.Delete && cursorPosition < fieldValue.length()) {
                fieldValue.deleteCharAt(cursorPosition);
            } else if (keyStroke.getKeyType() == KeyType.ArrowLeft && cursorPosition > 0) {
                cursorPosition--;
            } else if (keyStroke.getKeyType() == KeyType.ArrowRight && cursorPosition < fieldValue.length()) {
                cursorPosition++;
            } else if (keyStroke.getKeyType() == KeyType.Enter || keyStroke.getKeyType() == KeyType.Escape) {
                editing = false;
            }

            // Aktualizacja wartości
            configValues.put(fieldKeys[selectedField], fieldValue.toString());

            // Czyszczenie pola
            TextGraphics textGraphics = screen.newTextGraphics();
            textGraphics.setForegroundColor(TextColor.ANSI.YELLOW);
            textGraphics.fillRectangle(new TerminalPosition(x, y), new TerminalSize(15, 1), ' ');

            // Wyświetlanie zaktualizowanej wartości
            textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
            textGraphics.putString(x, y, fieldValue.toString());

            screen.setCursorPosition(new TerminalPosition(x + cursorPosition, y));
            screen.refresh();
        }

    }
    private void saveConfig() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode rootNode = objectMapper.createObjectNode();

            // Zapisz F (fryzjerzy)
            ObjectNode fNode = objectMapper.createObjectNode();
            fNode.put("S", Integer.parseInt(configValues.get("F_S")));
            fNode.put("M", Integer.parseInt(configValues.get("F_M")));
            fNode.put("G", Integer.parseInt(configValues.get("F_G")));
            rootNode.set("F", fNode);

            // Zapisz LF i WK
            rootNode.put("LF", Integer.parseInt(configValues.get("LF")));
            rootNode.put("WK", Integer.parseInt(configValues.get("WK")));

            // Zapisz do pliku
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(Paths.get("src/main/java/org/example/config.json").toFile(), rootNode);

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getConfigValues() {
        return configValues;
    }

    public boolean isConfigConfirmed() {
        return configConfirmed;
    }
}
