package net.crashcraft.whipclaim.visualize;

import org.bukkit.entity.Player;

import javax.xml.stream.Location;
import java.util.*;

public class VisualGroup {
    private ArrayList<Visual> activeVisuals;
    private Player player;
    private VisualizationManager manager;

    public VisualGroup(Player player, VisualizationManager manager) {
        this.player = player;
        this.manager = manager;
        activeVisuals = new ArrayList<>();
    }

    public void removeVisual(Visual visual){
        visual.remove();
        activeVisuals.remove(visual);
    }

    public void removeAllVisuals(){
        for (Visual visual : activeVisuals){
            visual.remove();
        }
    }

    public void removeAllVisualsOfType(VisualType type){
        for (Visual visual : activeVisuals){
            if (visual.getType().equals(type))
                visual.remove();
        }
    }

    public int generateUiniqueID(){
        int id = (int) (Math.random() * 1000000);
        for (Visual visual : activeVisuals){
            if (visual.containsID(id))
                return generateUiniqueID();
        }
        return id;
    }

    public UUID generateUiniqueUUID(){
        UUID uuid = UUID.randomUUID();
        for (Visual visual : activeVisuals){
            if (visual.containsUUID(uuid.toString()))
                return generateUiniqueUUID();
        }
        return uuid;
    }

    public ArrayList<Visual> getActiveVisuals() {
        return activeVisuals;
    }

    public Player getPlayer() {
        return player;
    }

    public VisualizationManager getManager() {
        return manager;
    }
}
