package com.lewis.brandon.primes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * @author	Brandon Lewis
 * @since	May 3, 2017
 */
public class PrimesGeneratorTest {
	PrimesGenerator pg = new PrimesGenerator();

	// ==================================================
	// 					Accuracy Tests
	// ==================================================

	@Test
	public void testZerosNotPrime() {
		assertFalse(pg.isPrime(0));
	}

	@Test
	public void testOneIsNotPrime() {
		assertFalse(pg.isPrime(1));
	}

	@Test
	public void testTwoIsPrime() {
		assertFalse(pg.isPrime(2));
	}

	@Test
	public void test7900IsNotPrime() {
		pg.generate(1, 7900);
		assertFalse(pg.isPrime(7900));
	}

	@Test
	public void test7919IsPrime() {
		pg.generate(1, 7920);
		assertTrue(pg.isPrime(7919));
	}

	@Test
	public void testGenerateFindsFirst26Primes() {
		List<Integer> expected = Arrays.asList(2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71,73,79,83,89,97,101);
		List<Integer> results = pg.generate(1, 101);
		assertTrue(results.equals(expected));
	}

	@Test
	public void testGeneratorFindsPrimesBetween7900and7920() {
		List<Integer> expected = Arrays.asList(7901, 7907, 7919);
		List<Integer> results = pg.generate(7900, 7920);
		assertTrue(results.equals(expected));
	}

	// ==================================================
	// 					isPrime
	// ==================================================
	@Test
	public void testIsPrimeReturnsFalseForNegativeNumbers() {
		assertFalse(pg.isPrime(-84));
	}

	@Test
	public void testIsPrimNotDependentOnPreviousInvocationOfGenerateMethod() {
		assertTrue(pg.isPrime(7919));
	}
	
	// ==================================================
	// 					generate
	// ==================================================

	@Test
	public void testGenerateAcceptsSameNumbers() {
		List<Integer> expected = Arrays.asList(11);
		List<Integer> primes = pg.generate(11, 11);
		assertTrue(primes.equals(expected));
	}
	
	@Test
	public void testGeneratorHandlesInverseRanges() {
		List<Integer> expected = Arrays.asList(2,3,5,7);
		List<Integer> primes = pg.generate(10, 1);
		assertTrue(primes.equals(expected));
	}
	
	@Test
	public void testGeneratorInverseRangesReturnsSameResultsAsStandardRanges() {
		List<Integer> standard = pg.generate(1, 200);
		List<Integer> inverse = pg.generate(200, 1);
		assertTrue(standard.equals(inverse));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGeneratorRejectsNegativeStartingValue() {
		pg.generate(-10, 1000);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGeneratorRejectsNegativeEndingValue() {
		pg.generate(10, -1000);
	}
}