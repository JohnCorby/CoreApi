package com.johncorby.coreapi.util.eventconversation;

import com.johncorby.coreapi.util.storedclass.Identifiable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

/**
 * Like the conversation api but works with events
 */
public class EventConversation extends Identifiable<Player> implements Listener {
    private EventPrompt firstPrompt, currentPrompt;
    private boolean abandoned;

    public EventConversation(Player forWhom) {
        super(forWhom);
    }

    public static EventConversation get(Player identity) {
        return get(EventConversation.class, identity);
    }

    @Override
    public boolean create() throws IllegalStateException {
        if (!super.create()) return false;

        if (firstPrompt == null)
            throw new IllegalStateException(this + " doesn't have first prompt");
        if (currentPrompt == null) {
            abandoned = false;
            currentPrompt = firstPrompt;
            outputNextPrompt();
        }

        return true;
    }

    @Override
    public boolean dispose() {
        if (!super.dispose()) return false;

        abandoned = currentPrompt == null;
        if (!abandoned) {
            abandoned = true;
            currentPrompt.unregister();
            currentPrompt = null;
        }

        return true;
    }

    public void setFirstPrompt(EventPrompt firstPrompt) {
        if (currentPrompt != null) return;
        this.firstPrompt = firstPrompt;
    }

    void acceptInput(Event input) {
        if (currentPrompt != null) {
            // Not abandoned, output the next prompt
            currentPrompt = currentPrompt.acceptInput(input);
            outputNextPrompt();
        }
    }

    private void outputNextPrompt() {
        if (currentPrompt == null) dispose();
        else {
            currentPrompt.register();
            get().sendRawMessage(currentPrompt.getPromptText());
        }
    }
}
