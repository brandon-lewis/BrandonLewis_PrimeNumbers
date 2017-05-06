package com.lewis.brandon.primes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author	Brandon Lewis
 * @since	May 3, 2017
 */
public class SievesGeneratorTest {
	PrimeNumberGenerator png = new SievesOfEratosthenesPrimesGenerator();
	static long startTime;
	
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
		png.generate(1, 7900);
		assertFalse(png.isPrime(7900));
	}

	@Test
	public void test7919IsPrime() {
		png.generate(1, 7920);
		assertTrue(png.isPrime(7919));
	}

	@Test
	public void test561IsNotPrime() {
		png.generate(561, 561);
		assertFalse(png.isPrime(561));
	}

	@Test
	public void test1729IsNotPrime() {
		png.generate(1729, 1729);
		assertFalse(png.isPrime(561));
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
	public void testIsPrimNotDependentOnPreviousInvocationOfGenerateMethod() {
		assertTrue(png.isPrime(7919));
	}
	
	// ==================================================
	// 					performance
	// ==================================================

	@Test
	public void testGeneratingSameRangeDoesNotInvokeRecalculation() {
		long startTime = Instant.now().toEpochMilli();
		png.generate(1, 100000);
		long firstRuntime = Instant.now().toEpochMilli() - startTime;
		
		startTime = Instant.now().toEpochMilli();
		png.generate(1, 100001);
		long secondRuntime = Instant.now().toEpochMilli() - startTime;
		System.out.println(" First Runtime: " + firstRuntime + "ms.");
		System.out.println("Second Runtime: " + secondRuntime + "ms.");
		assertTrue(secondRuntime < firstRuntime / 2);
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
}