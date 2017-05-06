package com.lewis.brandon.primes;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrialDivisionHybridPrimesGenerator implements PrimeNumberGenerator {
	
	private ConcurrentSkipListSet<Integer> primes = new ConcurrentSkipListSet<>();;
	private int calculatedUpTo = 1;
	
	private void generatePrimesBySieveOfEratosthenes(int endingValue) {
		ConcurrentSkipListSet<Integer> newPrimes;
		if(calculatedUpTo == 1) {
			System.out.println("Generating for the first time.");
			newPrimes = IntStream.iterate(3, (value) -> value + 2)
					.limit(endingValue/2).boxed().collect(Collectors.toCollection(ConcurrentSkipListSet::new));
			newPrimes.add(2);	// Add 2 explicitly, as it's the only even prime

			newPrimes.forEach(num -> {
				newPrimes.removeIf((targetNum) -> targetNum % num == 0 && targetNum != num);
			});
			
			primes.addAll(newPrimes);
			
			calculatedUpTo = endingValue;
		} else if(endingValue > calculatedUpTo) {
			System.out.println("Regenerating " + calculatedUpTo + " to " + endingValue + ".");
			int iterateFrom = calculatedUpTo % 2 == 0 ? calculatedUpTo + 1 : calculatedUpTo;
			newPrimes = IntStream.iterate(iterateFrom, (value) -> value + 2)
					.limit((endingValue-calculatedUpTo)/2).boxed().collect(Collectors.toCollection(ConcurrentSkipListSet::new));
			
			newPrimes.forEach(num -> {
				newPrimes.removeIf((targetNum) -> targetNum % num == 0 && targetNum != num);
			});
			
			primes.addAll(newPrimes);

			calculatedUpTo = endingValue;
		} else {
			System.out.println("No generation necessary!");
		}
		System.out.println("Primes Size: " + primes.size());
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
		// Swap startingValue and endingValue if endingValue is greater than startingValue
		if(startingValue > endingValue) {
			int tempValue = startingValue;
			startingValue = endingValue;
			endingValue = tempValue;
		}

		int endingValueSquareRoot = (int)Math.floor(Math.sqrt(endingValue));
		if(endingValueSquareRoot > calculatedUpTo) generatePrimesBySieveOfEratosthenes(endingValueSquareRoot);
		
		System.out.println("Checking " + startingValue + " to " + endingValue + "...");
		List<Integer> nums = IntStream.rangeClosed(startingValue, endingValue).filter(num -> isPrime(num)).boxed().collect(Collectors.toList());
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