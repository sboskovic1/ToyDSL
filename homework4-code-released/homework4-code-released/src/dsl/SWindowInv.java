package dsl;

import java.util.function.BinaryOperator;
import java.util.LinkedList;
import java.util.Queue;

// Efficient algorithm for aggregation over a sliding window.
// It assumes that there is a 'remove' operation for updating
// the aggregate when an element is evicted from the window.

public class SWindowInv<A> implements Query<A,A> {

	int wndSize;
    A agg;
    BinaryOperator<A> insert;
    BinaryOperator<A> remove;
    Queue<A> queue;

	public SWindowInv
	(int wndSize, A init, BinaryOperator<A> insert, BinaryOperator<A> remove)
	{
		if (wndSize < 1) {
			throw new IllegalArgumentException("window size should be >= 1");
		}
		this.wndSize = wndSize;
        this.agg = init;
        this.insert = insert;
        this.remove = remove;
        this.queue = new LinkedList<A>();
	}

	@Override
	public void start(Sink<A> sink) {
		// Do nothing
	}

	@Override
	public void next(A item, Sink<A> sink) {
		if (queue.size() == wndSize) {
            A evicted = queue.poll();
            agg = remove.apply(agg, evicted);
        }
        queue.add(item);
        agg = insert.apply(agg, item);
        if (queue.size() == wndSize) {
            sink.next(agg);
        }
	}

	@Override
	public void end(Sink<A> sink) {
		sink.end();
	}
	
}
