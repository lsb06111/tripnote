package edu.example.tripnote.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Service;

@Service
@ServerEndpoint(value="/ws/trip/{courseId}")
public class TripSocketHandler {

    private static final Map<String, List<Session>> sessionsByCourseId = new ConcurrentHashMap<>();

    public TripSocketHandler() {
        System.out.println("TripSocketHandler created!");
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("courseId") String courseId) {
        System.out.println("[Socket Open] Session ID: " + session.getId() + ", Course ID: " + courseId);
        sessionsByCourseId.computeIfAbsent(courseId, key -> Collections.synchronizedList(new ArrayList<>())).add(session);
    }

    @OnMessage
    public void onMessage(String msg, Session session) throws Exception {
        String courseId = getCourseIdFromSession(session);
        System.out.println("[Message Received] Course: " + courseId + ", Msg: " + msg);
        
        // Broadcast the message to all clients in the same course "room"
        if (courseId != null) {
            broadcast(courseId, msg);
        }
    }

    @OnClose
    public void onClose(Session session) {
        String courseId = getCourseIdFromSession(session);
        if (courseId != null) {
            List<Session> sessions = sessionsByCourseId.get(courseId);
            if (sessions != null) {
                sessions.remove(session);
                System.out.println("[Socket Close] Session ID: " + session.getId() + ", Course ID: " + courseId);
            }
        }
    }

    private void broadcast(String courseId, String msg) {
        List<Session> sessions = sessionsByCourseId.get(courseId);
        if (sessions != null) {
            System.out.println("Broadcasting to " + sessions.size() + " clients in course " + courseId);
            for (Session s : sessions) {
                try {
                    s.getBasicRemote().sendText(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getCourseIdFromSession(Session session) {
        // A helper to find which courseId a session belongs to
        return sessionsByCourseId.entrySet().stream()
                .filter(entry -> entry.getValue().contains(session))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}