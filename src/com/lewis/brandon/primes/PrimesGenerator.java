package com.lewis.brandon.primes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PrimesGenerator implements PrimeNumberGenerator {
	
	private ConcurrentSkipListSet<Integer> primes;
	private int calculatedUpTo;
	
	public static void main(String[] args) throws IOException {
		PrimesGenerator pg = new PrimesGenerator();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("==========================================");
		System.out.println("==   Brandon's Prime Number Generator   ==");
		System.out.println("==========================================");
		System.out.println("Directions:\n  - Enter a number range (e.g. 7900-7920) to see the prime numbers within that range (inclusive).\n");
		System.out.print(">> ");
		String input = br.readLine();
		while(!input.equals("exit")) {
			String regexPattern = "^(\\d+)-(\\d+)$";
			Pattern pattern = Pattern.compile(regexPattern);
			Matcher matcher = pattern.matcher(input);
			if(!matcher.find()) {
				System.out.println("Invalid command.  Enter a number range (e.g. 7900-7920) to see the prime numbers within that range (inclusive).");
			}
			int num1 = Integer.valueOf(matcher.group(1));
			int num2 = Integer.valueOf(matcher.group(2));
			List<Integer> primes = pg.generate(num1, num2);
			System.out.println("The Prime Numbers Between " + num1 + " and " + num2 + " are:");
			System.out.println("\t" + primes.toString().replaceAll("\\[|\\]", "") + "\n");
			System.out.print(">> ");
			input = br.readLine();
		}
	}
	
	private void generatePrimesBySieveOfEratosthenes(int endingValue) {
		if(endingValue > calculatedUpTo) {
			// Set up initial set, skipping all even numbers, starting from 3 because 1 is not prime
			primes = IntStream.iterate(3, (value) -> value + 2)
					.limit(endingValue/2).boxed().collect(Collectors.toCollection(ConcurrentSkipListSet::new));
			primes.add(2);	// Add 2 explicitly, as it's the only even prime
			
			primes.forEach(num -> {
				primes.removeIf((targetNum) -> targetNum % num == 0 && targetNum != num);
			});
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
		// Swap startingValue and endingValue if endingValue is greater than startingValue
		if(startingValue > endingValue) {
			int tempValue = startingValue;
			startingValue = endingValue;
			endingValue = tempValue;
		}

		generatePrimesBySieveOfEratosthenes(endingValue);
		
		List<Integer> nums = IntStream.rangeClosed(startingValue, endingValue).boxed().collect(Collectors.toList());
		nums.removeIf(num -> !isPrime(num));
		return nums;
	}

	/**
	 * Returns a boolean value indicating whether the provided number is prime.
	 * 
	 * @param value		the number to be check for primality
	 * @return			<code>true</code> if the number was determined to be prime; false if not prime
	 */
	@Override
	public boolean isPrime(int value) {
		if(value < 1) {
			throw new IllegalArgumentException("Negative numbers not supported, as Prime numbers are non-negative integers greater than 0");
		} else if(value == 1) {
			return false;
		} else if(value <= 3) {
			return true;
		} else {
			// Run generation method if isPrime() is invoked before 
			// generate() or if value is outside of previous calculations
			if(value > calculatedUpTo) {
				generatePrimesBySieveOfEratosthenes(value);
				return isPrime(value);
			} else {
				return primes.contains(value);
			}
		}
	}
}