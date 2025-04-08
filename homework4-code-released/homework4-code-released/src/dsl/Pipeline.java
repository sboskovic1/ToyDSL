package dsl;

// Serial composition.

public class Pipeline<A,B,C> implements Query<A,C> {

	Query<A,B> q1;
    Query<B,C> q2;
    SLastCount<B> sink1;
    long count;


	public Pipeline(Query<A,B> q1, Query<B,C> q2) {
		this.q1 = q1;
        this.q2 = q2;
        this.sink1 = new SLastCount<B>();
        q1.start(sink1);
        this.count = 0;
	}

	@Override
	public void start(Sink<C> sink) {
		q1.start(sink1);
        q2.start(sink);
	}

	@Override
	public void next(A item, Sink<C> sink) {
		q1.next(item, sink1);
        if (sink1.count != count) {
            count = sink1.count;
            q2.next(sink1.last, sink);
        }
	}

	@Override
	public void end(Sink<C> sink) {
		q1.end(sink1);
        sink.end();
	}
	
}
