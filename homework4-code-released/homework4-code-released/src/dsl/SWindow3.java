package dsl;

import utils.functions.Func3;

// Sliding window of size 3.

public class SWindow3<A,B> implements Query<A,B> {

	A item1;
    A item2;
    A item3;
    int size;
    Func3<A,A,A,B> op;

	public SWindow3(Func3<A,A,A,B> op) {
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
        } else if (size == 2) {
            item3 = item;
            size++;
            sink.next(op.apply(item1, item2, item3));
        } else {
            item1 = item2;
            item2 = item3;
            item3 = item;
            sink.next(op.apply(item1, item2, item3));
        }
	}

	@Override
	public void end(Sink<B> sink) {
		sink.end();
	}
	
}
