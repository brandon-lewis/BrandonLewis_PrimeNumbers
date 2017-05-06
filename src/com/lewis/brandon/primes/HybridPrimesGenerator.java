package com.lewis.brandon.primes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HybridPrimesGenerator implements PrimeNumberGenerator {
	private List<String> metrics = new ArrayList<>();
	private ConcurrentSkipListSet<Integer> primes = new ConcurrentSkipListSet<>();;
	private int calculatedUpTo = 1;
	
	public List<String> getMetrics() { return metrics; }
	
	private void generatePrimesBySieveOfEratosthenes(int endingValue) {
		ConcurrentSkipListSet<Integer> newPrimes;
		if(calculatedUpTo == 1) {
			long startTime = Instant.now().toEpochMilli();
			newPrimes = IntStream.iterate(3, (value) -> value + 2)
					.limit(endingValue/2).boxed().collect(Collectors.toCollection(ConcurrentSkipListSet::new));
			newPrimes.add(2);	// Add 2 explicitly, as it's the only even prime

			newPrimes.forEach(num -> {
				newPrimes.removeIf((targetNum) -> targetNum % num == 0 && targetNum != num);
			});
			
			primes.addAll(newPrimes);

			metrics.add("M" + (metrics.size() + 1) + ": [" + (Instant.now().toEpochMilli() - startTime) + "ms] Calculated " + newPrimes.size() +
					" Prime Numbers after testing " + (endingValue - calculatedUpTo) + " values (range: " + calculatedUpTo + " - " + endingValue + ")");
			
			calculatedUpTo = endingValue;
			
		} else if(endingValue > calculatedUpTo) {
			long startTime = Instant.now().toEpochMilli();
			int iterateFrom = calculatedUpTo % 2 == 0 ? calculatedUpTo + 1 : calculatedUpTo;
			newPrimes = IntStream.iterate(iterateFrom, (value) -> value + 2)
					.limit((endingValue-calculatedUpTo)/2).boxed().collect(Collectors.toCollection(ConcurrentSkipListSet::new));
			
			newPrimes.forEach(num -> {
				newPrimes.removeIf((targetNum) -> targetNum % num == 0 && targetNum != num);
			});
			
			primes.addAll(newPrimes);

			metrics.add("M" + (metrics.size() + 1) + ": [" + (Instant.now().toEpochMilli() - startTime) + "ms] Calculated " + newPrimes.size() +
					" Prime Numbers after testing " + (endingValue - calculatedUpTo) + " values (range: " + calculatedUpTo + " - " + endingValue + ")");
			calculatedUpTo = endingValue;
		}
	}

	/**
	 * Calculates the prime numbers within the given range.  The <code>startingValue</code>
	 * and <code>endingValue</code> values will be switched if the <code>startingValue</code>
	 * is larger than the <code>endingValue</code>.
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
		long startTime = Instant.now().toEpochMilli();
		// Swap startingValue and endingValue if endingValue is greater than startingValue
		if(startingValue > endingValue) {
			int tempValue = startingValue;
			startingValue = endingValue;
			endingValue = tempValue;
		}

		int endingValueSquareRoot = (int)Math.floor(Math.sqrt(endingValue));
		if(endingValueSquareRoot > calculatedUpTo) generatePrimesBySieveOfEratosthenes(endingValueSquareRoot);
		
		List<Integer> nums = IntStream.rangeClosed(startingValue, endingValue).filter(num -> isPrime(num)).boxed().collect(Collectors.toList());
		metrics.add("M" + (metrics.size() + 1) + ": [" + (Instant.now().toEpochMilli() - startTime) + "ms] generate(" + startingValue + ", " + endingValue +
				") method identifed " + nums.size() + " Prime Numbers");
		return nums;
	}

	/**
	 * Returns a boolean value indicating whether the provided number is prime.
	 * 
	 * @param value		the number to be check for primality
	 * @return			<code>true</code> if <b>value</b> is determined to be prime; false if not prime or if <b>value</b> is a negative number
	 */
	@Override
	public boolean isPrime(int value) {
		int endingValueSquareRoot = (int)Math.floor(Math.sqrt(value));
		if(value <= 1) {
			return false;
		} else {
			if(endingValueSquareRoot > calculatedUpTo)
				generatePrimesBySieveOfEratosthenes(endingValueSquareRoot);
			return primes.stream().noneMatch(prime -> prime != value && value % prime == 0);
		}
	}
}