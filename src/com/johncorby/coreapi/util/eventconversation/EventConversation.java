package com.johncorby.coreapi.util.eventconversation;

import com.johncorby.coreapi.util.storedclass.Identifiable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

/**
 * I'm hella gay
 */
public class EventConversation extends Identifiable<Player> implements Listener {
    protected EventPrompt currentPrompt;
    Map<Object, Object> sessionData;
    private EventPrompt firstPrompt;
    private boolean abandoned;

    public EventConversation(Player forWhom) {
        super(forWhom);
    }

    public static EventConversation get(Player identity) {
        return (EventConversation) get(EventConversation.class, identity);
    }

    @Override
    protected boolean create(Player identity) {
        sessionData = new HashMap<>();
        return super.create(identity);
    }

    @Override
    public boolean dispose() {
        if (!stored()) return false;
        abandon();
        return super.dispose();
    }

    public void begin() throws IllegalStateException {
        if (!exists())
            throw new IllegalStateException(this + " doesn't exist");
        if (firstPrompt == null)
            throw new IllegalStateException(this + " doens't have firstPrompt");
        if (currentPrompt == null) {
            abandoned = false;
            currentPrompt = firstPrompt;
            outputNextPrompt();
        }
    }

    public synchronized void abandon() throws IllegalStateException {
        if (!exists())
            throw new IllegalStateException(this + " doesn't exist");
        if (currentPrompt == null) {
            abandoned = true;
            return;
        }
        if (!abandoned) {
            abandoned = true;
            currentPrompt.unregister();
            currentPrompt = null;
        }
    }

    void acceptInput(Event input) {
        if (currentPrompt != null) {
            // Not abandoned, output the next prompt
            currentPrompt = currentPrompt.acceptInput(input);
            outputNextPrompt();
        }
    }

    private void outputNextPrompt() {
        if (currentPrompt == null) {
            abandon();
        } else {
            currentPrompt.register();
            get().sendRawMessage(currentPrompt.getPromptText());
        }
    }

    public ConversationState getState() {
        if (currentPrompt != null) {
            return ConversationState.STARTED;
        } else if (abandoned) {
            return ConversationState.ABANDONED;
        } else {
            return ConversationState.UNSTARTED;
        }
    }

    public void setFirstPrompt(EventPrompt firstPrompt) {
        if (currentPrompt != null) return;
        this.firstPrompt = firstPrompt;
    }

    public void setInitialSessionData(Map<Object, Object> initialSessionData) {
        if (currentPrompt != null) return;
        sessionData = initialSessionData;
    }

    public enum ConversationState {
        UNSTARTED,
        STARTED,
        ABANDONED
    }
}
