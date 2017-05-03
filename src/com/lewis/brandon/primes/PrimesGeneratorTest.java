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
	// 					isPrime
	// ==================================================
	@Test
	public void testRejectsNegativeNumbers() {
		assertFalse(pg.isPrime(-1));
	}

	@Test
	public void testZeroIsNotPrime() {
		assertFalse(pg.isPrime(0));
	}

	@Test
	public void testOneIsNotPrime() {
		assertFalse(pg.isPrime(1));
	}

	@Test
	public void test7900IsNotPrime() {
		assertFalse(pg.isPrime(7900));
	}

	@Test
	public void test7919IsPrime() {
		assertTrue(pg.isPrime(7919));
	}

	
	// ==================================================
	// 					generate
	// ==================================================
	@Test
	public void testGeneratorHandlesInverseRanges() {
		List<Integer> expected = Arrays.asList(2,3,4,7);
		List<Integer> primes = pg.generate(10, 1);
		assertTrue(primes.equals(expected));
	}
	@Test
	public void testGeneratorInverseRangesReturnsSameResultsAsStandardRanges() {
		List<Integer> standard = pg.generate(15, 30);
		List<Integer> inverse = pg.generate(30, 15);
		assertTrue(standard.equals(inverse));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGeneratorRejectsANegativeStartingValue() {
		pg.generate(-10, 1000);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGeneratorRejectsANegativeEndingValue() {
		pg.generate(10, -1000);
	}

	@Test
	public void testGeneratorFindsPrimesBetween7900and7920() {
		List<Integer> expected = Arrays.asList(7901, 7907, 7919);
		List<Integer> results = pg.generate(7900, 7920);
		assertTrue(results.equals(expected));
	}
}