package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.lanterna.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {

        GUIStartingWindow guiStartingWindow = new GUIStartingWindow();

//        Wczytaj dane z pliku
        byte[] jsonData = Files.readAllBytes(Paths.get("src/main/java/org/example/config.json"));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(jsonData);
        JsonNode fNode = node.get("F");
        JsonNode wkNode = node.get("WK");
        JsonNode lfNode = node.get("LF");

        int WielkoscKolejki = wkNode.asInt();
        int LiczbaFoteli = lfNode.asInt();

        int[] ListaFryzjerow = {fNode.get("S").asInt(), fNode.get("M").asInt(), fNode.get("G").asInt()};


        QueueSharedResource queue = new QueueSharedResource(WielkoscKolejki);
        ChairsSharedResource chairs = new ChairsSharedResource(LiczbaFoteli);
        HairDressersSharedResource hairdresser = new HairDressersSharedResource(ListaFryzjerow);
        GUIHandler gui = new GUIHandler(queue, chairs, hairdresser, WielkoscKolejki, LiczbaFoteli);
        QueueThread qt = new QueueThread(queue);
        qt.start();

        SchedulerThread[] st = new SchedulerThread[LiczbaFoteli];
        for(int i=0; i<LiczbaFoteli; i++){
            st[i] = new SchedulerThread(queue, chairs, hairdresser);
            st[i].start();
        }
    }
}