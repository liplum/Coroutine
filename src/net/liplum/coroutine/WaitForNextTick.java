package net.liplum.coroutine;

public class WaitForNextTick implements IWaitable{
    private int tick = 2;
    @Override
    public void OnTick() {
        tick--;
    }

    @Override
    public boolean isFinished() {
        return tick <= 0;
    }
}
