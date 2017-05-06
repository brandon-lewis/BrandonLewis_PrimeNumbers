package com.lewis.brandon.primes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrimesMain {

	public static void main(String[] args) throws IOException {
		HybridPrimesGenerator pg = new HybridPrimesGenerator();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("==========================================");
		System.out.println("==   Brandon's Prime Number Generator   ==");
		System.out.println("==========================================");
		System.out.println("Directions:\n  - Enter a number range (e.g. 7900-7920) to see the prime numbers within that range (inclusive).");
		System.out.println("  - Type metrics to see the runtime metrics of previous runs.\n  - Type exit to quit.");
		System.out.print(">> ");
		String input = br.readLine();
		while(!input.equals("exit")) {
			if(input.equals("metrics")) {
				if(pg.getMetrics().size() == 0) {
					System.out.println("You must ask for a range of primes at least once before you can view metrics!");
				} else {
					pg.getMetrics().forEach(metric -> System.out.println("\t" + metric));
				}
			} else {
				String regexPattern = "^(\\d+)-(\\d+)$";
				Pattern pattern = Pattern.compile(regexPattern);
				Matcher matcher = pattern.matcher(input);
				if(!matcher.find()) {
					System.out.println("Invalid command.  Enter a number range (e.g. 7900-7920) or metrics to view basic runtime stats.");
				} else {
					int num1 = Integer.valueOf(matcher.group(1));
					int num2 = Integer.valueOf(matcher.group(2));
					List<Integer> primes = pg.generate(num1, num2);
					if(primes.size() > 0) {
						System.out.println("The " + primes.size() + " Prime Numbers Between " + num1 + " and " + num2 + " are:");
						System.out.println("\t" + primes.toString().replaceAll("\\[|\\]", ""));	
					} else {
						System.out.println("There are no Prime Numbers between " + num1 + " and " + num2);
					}
				}
			}
			System.out.print("\n>> ");
			input = br.readLine();
		}
	}
}