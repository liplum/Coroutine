package net.liplum.coroutine;

public class NotWait implements net.liplum.coroutine.IWaitable {
    @Override
    public void onTick() {

    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
