package com.lewis.brandon.primes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrimesMain {

	public static void main(String[] args) throws IOException {
		PrimeNumberGenerator pg = new TrialDivisionHybridPrimesGenerator();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("==========================================");
		System.out.println("==   Brandon's Prime Number Generator   ==");
		System.out.println("==========================================");
		System.out.println("Directions:\n  - Enter a number range (e.g. 7900-7920) to see the prime numbers within that range (inclusive).\n");
		System.out.println("  - Type -metrics to see the runtime metrics of the last 3 runs.\n");
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
			System.out.println("The " + primes.size() + " Prime Numbers Between " + num1 + " and " + num2 + " are:");
			System.out.println("\t" + primes.toString().replaceAll("\\[|\\]", "") + "\n");
			System.out.print(">> ");
			input = br.readLine();
		}
	}
}