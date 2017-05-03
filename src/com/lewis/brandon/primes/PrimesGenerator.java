package com.lewis.brandon.primes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PrimesGenerator implements PrimeNumberGenerator {
	
	public static void main(String[] args) {
		PrimesGenerator pg = new PrimesGenerator();
		pg.generate(20, 1).forEach(System.out::println);
	}

	/**
	 * Calculates the prime numbers within the given range.  The <i>startingValue</i>
	 * and <i>endingValue</i> values will be switched if the <i>startingValue</i>
	 * is larger than the <i>endingValue</i>.
	 * 
	 * @param startingValue		the number to start generating at (inclusive)
	 * @param endingValue		the number to stop generating at (inclusive)
	 * @return 					ordered ArrayList of prime numbers within the given range.
	 */
	@Override
	public List<Integer> generate(int startingValue, int endingValue) {
		if(startingValue < 1 || endingValue < 1) {
			throw new IllegalArgumentException("Neither startingValue nor endingValue can be less than 1.");
		}
		if(startingValue > endingValue) {
			int tempValue = startingValue;
			startingValue = endingValue;
			endingValue = tempValue;
		}
		return IntStream.rangeClosed(endingValue, startingValue).filter(this::isPrime)
				.boxed().collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public boolean isPrime(int value) {
		if(value <= 1) {
			return false;
		} else if(value == 2) {
			return true;
		} else {
			return true;
		}
	}
}