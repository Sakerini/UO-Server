package com.noetic.server.utils;

import com.noetic.server.GameServer;
import com.noetic.server.domain.model.Zone;
import com.noetic.server.enums.LogType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ZoneParser implements Runnable {

    public enum State {
        Loading,
        Finished
    }
    private State state;

    private GameServer server;
    private Thread thread;

    private File mapsFolder = new File("src/main/resources/data/maps/");
    private List<File> mapsToParse;

    public ZoneParser(GameServer server) {
        this.server = server;
        mapsToParse = new ArrayList<>();
        for (File file : mapsFolder.listFiles()) {
            if (file.isFile()) {
                mapsToParse.add(file);
            }
        }
    }

    public synchronized void start() {
        GameServer.getServerConsole().writeMessage(LogType.Server, String.format("Loading %s zone(s)...", mapsToParse.size()));

        thread = new Thread(this, "parser");
        thread.start();
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        state = State.Loading;
        do {
            File map = mapsToParse.get(0);
            Zone zone = new Zone();

            Document mapDoc = null;
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                mapDoc = dBuilder.parse(map);
                mapDoc.getDocumentElement().normalize();
            } catch (SAXException | IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            NodeList properties = mapDoc.getElementsByTagName("property");
            for (int i = 0; i < properties.getLength(); i++) {
                Node propertyNode = properties.item(i);

                if (propertyNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element propertyElement = (Element) propertyNode;

                    String name = propertyElement.getAttribute("name");
                    switch (name.toLowerCase()) {
                        case "zone_id":
                            zone.setId(Integer.parseInt(propertyElement.getAttribute("value")));
                            break;
                        case "zone_name":
                            zone.setName(propertyElement.getAttribute("value"));
                            break;
                    }
                }
            }

            ZoneManager.addZone(zone);
            mapsToParse.remove(map);
        } while (mapsToParse.size() > 0);
        long stop = System.currentTimeMillis();
        GameServer.getServerConsole().writeMessage(LogType.Server, String.format("Loaded zones in %sms.", stop - start));
        state = State.Finished;
    }

    public State getState() {
        return state;
    }
}
