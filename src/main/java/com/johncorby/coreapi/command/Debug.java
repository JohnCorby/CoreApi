package com.johncorby.coreapi.command;

import com.johncorby.coreapi.util.MessageHandler;
import com.johncorby.coreapi.util.storedclass.StoredClass;
import org.bukkit.entity.Player;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;

public class Debug extends BaseCommand {
    Debug() {
        super("Get debug stuff", "", PERM_ADMIN);
    }

    @Override
    public boolean onCommand(Player sender, String[] args) {
        //debug(Thread.getAllStackTraces());
        for (StoredClass c : StoredClass.getClasses())
            try {
                Object[] debug = c.getDebug().toArray();
                if (debug == null) continue;
                MessageHandler.debug(debug);
            } catch (Exception e) {
                MessageHandler.debug("Error getting debug for " + c + ": " + getStackTrace(e));
            }
        MessageHandler.info(sender, "See console");
        return true;
    }
}
