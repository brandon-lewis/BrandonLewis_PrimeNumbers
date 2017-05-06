package com.lewis.brandon.primes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author	Brandon Lewis
 * @since	May 3, 2017
 */
public class TrialDivisionHybridGeneratorTest {
	PrimeNumberGenerator png;
	static long startTime;
	
	@Before
	public void before() {
		png = new TrialDivisionHybridPrimesGenerator();
	}
	
	@BeforeClass
	public static void beforeClass() {
		startTime = Instant.now().toEpochMilli();
	}
	
	@AfterClass
	public static void afterClass() {
		System.out.println("Time to run all tests: " + (Instant.now().toEpochMilli() - startTime) + "ms.");
	}

	// ==================================================
	// 					Accuracy Tests
	// ==================================================

	@Test
	public void testZerosNotPrime() {
		assertFalse(png.isPrime(0));
	}

	@Test
	public void testOneIsNotPrime() {
		assertFalse(png.isPrime(1));
	}

	@Test
	public void testTwoIsPrime() {
		assertTrue(png.isPrime(2));
	}

	@Test
	public void test7900IsNotPrime() {
		assertFalse(png.isPrime(7900));
	}

	@Test
	public void test7919IsPrime() {
		png.generate(7919, 7919);
		assertTrue(png.isPrime(7919));
	}

	@Test
	public void testGenerateFindsFirst26Primes() {
		List<Integer> expected = Arrays.asList(2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71,73,79,83,89,97,101);
		List<Integer> results = png.generate(1, 101);
		assertTrue(results.equals(expected));
	}

	@Test
	public void testGeneratorFindsPrimesBetween7900and7920() {
		List<Integer> expected = Arrays.asList(7901, 7907, 7919);
		List<Integer> results = png.generate(7900, 7920);
		assertTrue(results.equals(expected));
	}
	
	// ==================================================
	// 					isPrime
	// ==================================================
	@Test
	public void testIsPrimeReturnsFalseForNegativeNumbers() {
		assertFalse(png.isPrime(-84));
	}

	@Test
	public void testIsPrimeNotDependentOnPreviousInvocationOfGenerateMethod() {
		assertTrue(png.isPrime(7919));
	}
	
	// ==================================================
	// 					generate
	// ==================================================

	@Test
	public void testGenerateAcceptsSameNumbers() {
		List<Integer> expected = Arrays.asList(11);
		List<Integer> primes = png.generate(11, 11);
		assertTrue(primes.equals(expected));
	}
	
	@Test
	public void testGeneratorHandlesInverseRanges() {
		List<Integer> expected = Arrays.asList(2,3,5,7);
		List<Integer> primes = png.generate(10, 1);
		assertTrue(primes.equals(expected));
	}
	
	@Test
	public void testGeneratorInverseRangesReturnsSameResultsAsStandardRanges() {
		List<Integer> standard = png.generate(1, 200);
		List<Integer> inverse = png.generate(200, 1);
		assertTrue(standard.equals(inverse));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGeneratorRejectsNegativeStartingValue() {
		png.generate(-10, 1000);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGeneratorRejectsNegativeEndingValue() {
		png.generate(10, -1000);
	}

	// ==================================================
	// 					performance
	//
	//	Note: Mileage may vary by processing power
	// ==================================================

	@Test
	public void testGeneratingSameRangeDoesNotInvokeRecalculation() {
		PrimeNumberGenerator pngTemp = new TrialDivisionHybridPrimesGenerator();
		long startTime = Instant.now().toEpochMilli();
		pngTemp.generate(1, 100_000);
		long firstRuntime = Instant.now().toEpochMilli() - startTime;
		
		startTime = Instant.now().toEpochMilli();
		pngTemp.generate(1, 100_000);
		long secondRuntime = Instant.now().toEpochMilli() - startTime;
		System.out.println("1-100,000 1st Run: " + firstRuntime + "ms");
		System.out.println("1-100,000 2nd Run: " + secondRuntime + "ms");
	}

	@Test
	public void testGeneratingForLargerRangeThanPreviousRangesOnlyGeneratesForNewRange() {
		PrimeNumberGenerator pngTemp = new TrialDivisionHybridPrimesGenerator();
		long startTime = Instant.now().toEpochMilli();
		pngTemp.generate(1, 100_000);
		long firstRuntime = Instant.now().toEpochMilli() - startTime;
		
		startTime = Instant.now().toEpochMilli();
		pngTemp.generate(1, 200_000);
		long secondRuntime = Instant.now().toEpochMilli() - startTime;
		System.out.println("1-100,000 Run: " + firstRuntime + "ms");
		System.out.println("1-200,000 Run: " + secondRuntime + "ms");
	}
}