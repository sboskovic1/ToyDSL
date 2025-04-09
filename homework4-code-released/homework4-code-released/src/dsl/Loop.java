package dsl;

import utils.Or;

// Feedback composition.

public class Loop<A,B> implements Query<A,B> {

    Query<Or<A,B>,B> q;
    B last;
    SLastCount<B> sink1;
    long prevCount;

	public Loop(Query<Or<A,B>,B> q) {
		this.q = q;
        this.sink1 = new SLastCount<B>();
        this.prevCount = 0;
	}

	@Override
	public void start(Sink<B> sink) {
        q.start(sink1);
        if (sink1.count != prevCount) {
            prevCount = sink1.count;
            q.next(Or.inr(sink1.last), sink1);
            sink.next(sink1.last);
        }
    }

	@Override
	public void next(A item, Sink<B> sink) {
        q.next(Or.inl(item), sink1);
        if (sink1.count != prevCount) {
            prevCount = sink1.count;
            sink.next(sink1.last);
            q.next(Or.inr(sink1.last), sink);
        }
	}

	@Override
	public void end(Sink<B> sink) {
		sink.end();
	}
	
}
