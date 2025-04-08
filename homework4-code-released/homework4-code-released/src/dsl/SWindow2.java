package dsl;

import utils.functions.Func2;

// Sliding window of size 2.

public class SWindow2<A,B> implements Query<A,B> {

	A item1;
    A item2;
    int size;
    Func2<A,A,B> op;


	public SWindow2(Func2<A,A,B> op) {
		this.size = 0;
        this.op = op;
	}

	@Override
	public void start(Sink<B> sink) {
		// Do nothing
	}

	@Override
	public void next(A item, Sink<B> sink) {
		if (size == 0) {
            item1 = item;
            size++;
        } else if (size == 1) {
            item2 = item;
            size++;
            sink.next(op.apply(item1, item2));
        } else {
            item1 = item2;
            item2 = item;
            sink.next(op.apply(item1, item2));
        }
	}

	@Override
	public void end(Sink<B> sink) {
		sink.end();
	}
	
}
