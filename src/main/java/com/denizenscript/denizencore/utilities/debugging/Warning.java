package com.denizenscript.denizencore.utilities.debugging;

import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.queues.ScriptQueue;

public class Warning {

    public String message;

    public Warning(String message) {
        this.message = message;
    }

    public void warn(ScriptEntry entry) {
        warn(entry == null ? null : entry.getResidingQueue());
    }

    public void warn() {
        warn((ScriptQueue) null);
    }

    public void warn(ScriptQueue queue) {
        Debug.echoError(queue, message);
    }
}
