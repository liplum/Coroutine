package net.liplum.coroutine;

public class WaitForTicks implements IWaitable {
    private int tick;

    public WaitForTicks(int tick) {
        this.tick = tick+1;
    }

    @Override
    public void onTick() {
        tick--;
    }

    @Override
    public boolean isFinished() {
        return tick <= 0;
    }
}
