package com.johncorby.coreapi.util.storedclass;

import com.johncorby.coreapi.util.Runnable;

public abstract class IdentTask<I> extends Identifiable<I> {
    protected Task task = new Task();

    public IdentTask(I identity) {
        super(identity);
    }

    protected abstract void run();

    @Override
    public boolean dispose() {
        if (!task.isCancelled()) task.cancel();
        return !exists();
    }

    protected final class Task extends Runnable {
        @Override
        public final void run() {
            IdentTask.this.run();
        }

        @Override
        public final synchronized void cancel() {
            super.cancel();
            IdentTask.super.dispose();
        }
    }
}
