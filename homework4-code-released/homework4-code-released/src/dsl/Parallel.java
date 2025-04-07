package dsl;

import java.util.Queue;

import utils.functions.Func2;

import java.util.LinkedList;

// A variant of parallel composition, which is similar to 'zip'.

public class Parallel<A, B, C, D> implements Query<A, D> {

	Query<A,B> q1;
    Query<A,C> q2;
    Func2<B,C,D> op;
    SLastCount<B> sink1;
    SLastCount<C> sink2;
    Queue<B> queue1;
    Queue<C> queue2;
    long count1;
    long count2;

	public Parallel(Query<A,B> q1, Query<A,C> q2, Func2<B,C,D> op) {
		this.q1 = q1;
        this.q2 = q2;
        this.op = op;
        this.sink1 = new SLastCount<>();
        this.sink2 = new SLastCount<>();
        this.queue1 = new LinkedList<>();
        this.queue2 = new LinkedList<>();
        this.count1 = 0;
        this.count2 = 0;
	}

	@Override
	public void start(Sink<D> sink) {
		q1.start(sink1);
        q2.start(sink2);
	}

	@Override
	public void next(A item, Sink<D> sink) {
        q1.next(item, sink1);
        q2.next(item, sink2);
        if (sink1.count != count1) {
            queue1.add(sink1.last);
            count1 = sink1.count;
        }
        if (sink2.count != count2) {
            queue2.add(sink2.last);
            count2 = sink2.count;
        }

        if (queue1.size() != 0 && queue2.size() != 0) {
            sink.next(op.apply(queue1.poll(), queue2.poll()));
        }
	}

	@Override
	public void end(Sink<D> sink) {
		sink1.end();
        sink2.end();
        sink.end();
	}
	
}
