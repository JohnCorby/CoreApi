package com.johncorby.coreapi.command;

import com.johncorby.coreapi.util.MessageHandler;
import com.johncorby.coreapi.util.storedclass.StoredObject;
import org.bukkit.entity.Player;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;

public class Debug extends BaseCommand {
    Debug() {
        super("Get debug stuff", "", PERM_ADMIN);
    }

    @Override
    public boolean onCommand(Player sender, String[] args) {
        //debug(Thread.getAllStackTraces());
        for (StoredObject c : StoredObject.getClasses())
            try {
                if (c.getDebug() == null) continue;
                MessageHandler.debug(c.getDebug().toArray());
            } catch (Exception e) {
                MessageHandler.debug("Error getting debug for " + c + ": " + getStackTrace(e));
            }
        MessageHandler.info(sender, "See console");
        return true;
    }
}
