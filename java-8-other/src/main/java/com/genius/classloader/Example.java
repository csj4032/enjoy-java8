package com.genius.classloader;

public class Example implements IExample {

	private int counter;
	private ILeak leak;

	private static final long[] cache = new long[50000000];

	@Override
	public String message() {
		return "Version 1";
	}

	@Override
	public int plusPlus() {
		return counter++;
	}

	@Override
	public int counter() {
		return counter;
	}

	@Override
	public IExample copy(IExample example) {
		if (example != null) {
			counter = example.counter();
			leak = example.leak();
		}
		return this;
	}

	@Override
	public ILeak leak() {
		return new Leak(leak);
	}
}
