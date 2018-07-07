package com.johncorby.coreapi.util.storedclass;

public abstract class IdentTask<I> extends Identifiable<I> {
    protected Task task;

    public IdentTask(I identity) {
        super(identity);
    }

    protected boolean create(I identity) {
        if (!super.create(identity)) return false;
        task = new Task();
        return true;
    }

    protected abstract void run();

    @Override
    public boolean dispose() {
        if (!task.isCancelled()) task.cancel();
        return stored();
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
