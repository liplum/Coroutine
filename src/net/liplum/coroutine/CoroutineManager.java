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

    private final LinkedList<IEnumerable> _coroutineList = new LinkedList<>();

    public void startCoroutine(IEnumerable i) {
        _coroutineList.addLast(i);
    }

    public void StopCoroutine(IEnumerable i) {
        _coroutineList.remove(i);
    }

    public void OnTick() {
        LinkedList<IEnumerable> needRemoves = new LinkedList<>();
        for (IEnumerable ie : _coroutineList) {

            boolean hasRest = true;
            IEnumerator ier = ie.getEnumerator();
            Object currentResult = ier.getCurrent();
            if (currentResult instanceof IWaitable) {
                IWaitable wait = (IWaitable) currentResult;
                wait.OnTick();
                if (wait.isFinished()) {
                    hasRest = ier.MoveNext();
                }
            }else {
                hasRest = ier.MoveNext();
            }

            if (!hasRest) {
                needRemoves.addLast(ie);
            }
        }
        for (IEnumerable<IWaitable> needRemove : needRemoves) {
            _coroutineList.remove(needRemove);
        }
    }
}
