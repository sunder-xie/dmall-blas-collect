package com.dmall.blas.collect.util;

public class ObjectFour<S, T, D, F> {
	private S first = null;
	private T second = null;
	private D third = null;
	private F fourth = null;

	public ObjectFour(S first, T second, D third, F fourth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
	}

	public S getFirst() {
		return first;
	}

	public void setFirst(S first) {
		this.first = first;
	}

	public T getSecond() {
		return second;
	}

	public void setSecond(T second) {
		this.second = second;
	}

	public D getThird() {
		return third;
	}

	public void setThird(D third) {
		this.third = third;
	}

	public F getFourth() {
		return fourth;
	}

	public void setFourth(F fourth) {
		this.fourth = fourth;
	}

	public static <S, T, D, F> ObjectFour<S, T, D, F> MakeObject(S s, T t, D d, F f) {
		return new ObjectFour<S, T, D, F>(s, t, d, f);
	}
}
