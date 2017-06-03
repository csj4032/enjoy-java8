package covariance;

import java.util.List;

/**
 * Created by Genius on 2017-06-03.
 */
public class CoVarianceTest {

	public class A {
	}

	public class B extends A {
	}

	public class C extends B {
	}

	public static void main(String[] args) {

	}

	private void testCoVariance(List<? extends B> list) {
		//list.add(new C());
		list.get(0);
	}

	private void testContraVariance(List<? super B> list) {
		list.add(new C());
		//C c = list.get(0);
	}
}
