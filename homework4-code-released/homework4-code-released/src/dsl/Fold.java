package dsl;

import utils.functions.Func2;

// Aggregation (one output item when the stream ends).

public class Fold<A, B> implements Query<A, B> {

	B agg;
    Func2<B,A,B> op;

	public Fold(B init, Func2<B,A,B> op) {
		this.agg = init;
        this.op = op;
	}

	@Override
	public void start(Sink<B> sink) {
		// Nothing to do
	}

	@Override
	public void next(A item, Sink<B> sink) {
		agg = op.apply(agg, item);
	}

	@Override
	public void end(Sink<B> sink) {
        sink.next(agg);
		sink.end();
	}
	
}
