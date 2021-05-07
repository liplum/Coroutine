package net.liplum.coroutine;

import net.liplum.enumerator.IEnumerable;
import net.liplum.enumerator.IEnumerator;

import java.util.LinkedList;

public final class CoroutineManager {
    private static CoroutineManager _instance = null;

    public static CoroutineManager Instance() {
        if (_instance == null) {
            _instance = new CoroutineManager();
        }
        return _instance;
    }

    private CoroutineManager() {
    }

    private final LinkedList<Coroutine> _coroutineList = new LinkedList<>();

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
        _coroutineList.addLast(task);
        return task;
    }

    public void StopCoroutine(Coroutine coroutine) {
        _coroutineList.remove(coroutine);
    }

    public void OnTick() {
        LinkedList<Coroutine> needRemoves = new LinkedList<>();
        for (Coroutine c : _coroutineList) {
            c.onTick();
            if (c.isDead()) {
                needRemoves.addLast(c);
            }
            else {
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
            _coroutineList.remove(needRemove);
        }
    }
}
