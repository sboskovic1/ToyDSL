package dsl;

import java.util.*;

import utils.functions.Func2;

// Naive algorithm for aggregation over a sliding window.

public class SWindowNv<A,B> implements Query<A,B> {

	int wndSize;
    Queue<A> queue;
    Func2<B,A,B> op;
    B init;

	public SWindowNv(int wndSize, B init, Func2<B,A,B> op) {
		if (wndSize < 1) {
			throw new IllegalArgumentException("window size should be >= 1");
		}
        this.wndSize = wndSize;
        this.queue = new LinkedList<A>();
        this.op = op;
        this.init = init;
	}

	@Override
	public void start(Sink<B> sink) {
		// Do nothing
	}

	@Override
	public void next(A item, Sink<B> sink) {
        if (queue.size() == wndSize) {
            queue.poll();
        }
		if (queue.size() == wndSize - 1) {
            queue.add(item);
            Iterator<A> it = queue.iterator();
            B agg = init;
            while (it.hasNext()) {
                A a = it.next();
                agg = op.apply(agg, a);
            }
            sink.next(agg);
        } else {
            queue.add(item);
        }
	}

	@Override
	public void end(Sink<B> sink) {
		sink.end();
	}
	
}
