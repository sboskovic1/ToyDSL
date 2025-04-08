package dsl;

import utils.functions.Func2;

// Running aggregation (one output item per input item).

public class Scan<A, B> implements Query<A, B> {

	B agg;
    Func2<B,A,B> op;

	public Scan(B init, Func2<B,A,B> op) {
		this.agg = init;
        this.op = op;
	}

	@Override
	public void start(Sink<B> sink) {
		// Do nothing
	}

	@Override
	public void next(A item, Sink<B> sink) {
		agg = op.apply(agg, item);
        sink.next(agg);
	}

	@Override
	public void end(Sink<B> sink) {
		sink.end();
	}
	
}
