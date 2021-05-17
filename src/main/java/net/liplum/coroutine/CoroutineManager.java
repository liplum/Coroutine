package net.liplum.coroutine;

import net.liplum.enumerator.IEnumerable;
import net.liplum.enumerator.IEnumerator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CoroutineManager {
    public CoroutineManager() {
    }

    private final LinkedList<Coroutine> coroutinesList = new LinkedList<>();

    public Coroutine startCoroutine(IEnumerable task) {
        Coroutine c = new Coroutine(task);
        startCoroutine(c);
        return c;
    }

    public Coroutine startCoroutine(IEnumerable task, int lifeSpan) {
        Coroutine c = new Coroutine(task, lifeSpan);
        startCoroutine(c);
        return c;
    }

    public Coroutine startCoroutine(Coroutine task) {
        coroutinesList.addLast(task);
        return task;
    }

    public Coroutine[] startCoroutines(Coroutine... coroutines) {
        Coroutine[] all = new Coroutine[coroutines.length];
        for (int i = 0; i < coroutines.length; i++) {
            Coroutine current = coroutines[i];
            coroutinesList.addLast(current);
            all[i] = current;
        }
        return all;
    }

    public void stopAll() {
        coroutinesList.clear();
    }

    public void StopCoroutine(Coroutine coroutine) {
        coroutinesList.remove(coroutine);
    }

    public void OnTick() {
        LinkedList<Coroutine> needRemoves = new LinkedList<>();
        for (Coroutine c : coroutinesList) {
            c.onTick();
            if (c.isDead()) {
                needRemoves.addLast(c);
            } else {
                boolean hasRest = true;
                IEnumerable ie = c.getTask();
                IEnumerator ier = ie.getEnumerator();
                Object currentResult = ier.getCurrent();
                if (currentResult instanceof IWaitable) {
                    IWaitable wait = (IWaitable) currentResult;
                    wait.onTick();
                    if (wait.isFinished()) {
                        hasRest = ier.MoveNext();
                    }
                } else {
                    hasRest = ier.MoveNext();
                }

                if (!hasRest) {
                    needRemoves.addLast(c);
                }
            }
        }
        for (Coroutine needRemove : needRemoves) {
            coroutinesList.remove(needRemove);
        }
    }
}
