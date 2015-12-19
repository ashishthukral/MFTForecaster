package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.HelperUtil;

public class Forecaster {

	private static final double INFLATION_PERCENTAGE = 3.5;

	private static final int NO_OF_YEARS = 20;

	private static final int NO_OF_ITERATIONS = 10_000;

	private static final double BASE_AMOUNT = 100_000;

	// Conservative Return
	private static final double MEAN_CONSERVATIVE = 6.189;

	// Conservative Risk
	private static final double SD_CONSERVATIVE = 6.3438;

	// Aggressive Return
	private static final double MEAN_AGGRESSIVE = 9.4324;

	// Aggressive Risk
	private static final double SD_AGGRESSIVE = 15.675;

	public static void main(String[] args) {

		Forecaster forecaster = new Forecaster();

		HelperUtil.print("*** Calculating Conservative Profile ***     Mean={0} , Standard Deviation={1}", MEAN_CONSERVATIVE, SD_CONSERVATIVE);
		forecaster.calculateProjections(MEAN_CONSERVATIVE, SD_CONSERVATIVE);

		HelperUtil.print("\n*** Calculating Aggressive Profile ***     Mean={0} , Standard Deviation={1}", MEAN_AGGRESSIVE, SD_AGGRESSIVE);
		forecaster.calculateProjections(MEAN_AGGRESSIVE, SD_AGGRESSIVE);
	}

	/**
	 * Run iterations for projecting returns. Prints the output at end.
	 * 
	 * @param mean
	 * @param sd
	 */
	public void calculateProjections(final double mean, final double sd) {

		final long startTime = System.currentTimeMillis();

		final List<Double> results = new ArrayList<>(NO_OF_ITERATIONS);

		/*
		 * NOTE - Using serial operations here as earlier using ExecutorService lead to higher running times probably due to more overheard
		 * of maintaining threads than actual effort of projecting returns.
		 * For thread pool size of even 100, time taken was around 5 times more than a simple for loop.
		 */
		for (int i = 0; i < NO_OF_ITERATIONS; i++) {
			results.add(project(mean, sd));
		}
		// Sort to get percentiles, median. O(n log n) complexity.
		Collections.sort(results);

		final Double percentile10 = results.get(HelperUtil.calculatePercentileIndex(10, results.size()));
		final double median = HelperUtil.calculateMedian(results);
		final Double percentile90 = results.get(HelperUtil.calculatePercentileIndex(90, results.size()));

		HelperUtil.print("10% percentile={0}", HelperUtil.round(percentile10));
		HelperUtil.print("Median={0}", HelperUtil.round(median));
		HelperUtil.print("90% percentile={0}", HelperUtil.round(percentile90));

		System.out.println("\nAfter Adjusting Inflation\n");

		HelperUtil.print("10% percentile={0}", HelperUtil.round(HelperUtil.adjustBackwardInflation(INFLATION_PERCENTAGE, NO_OF_YEARS, percentile10)));
		HelperUtil.print("Median={0}", HelperUtil.round(HelperUtil.adjustBackwardInflation(INFLATION_PERCENTAGE, NO_OF_YEARS, median)));
		HelperUtil.print("90% percentile={0}", HelperUtil.round(HelperUtil.adjustBackwardInflation(INFLATION_PERCENTAGE, NO_OF_YEARS, percentile90)));

		long endTime = System.currentTimeMillis();
		HelperUtil.print("Took {0} ms", endTime - startTime);
	}

	/**
	 * Project for {@link #NO_OF_YEARS} starting from the {@link #BASE_AMOUNT}. Adjusts mean every year by a random SD value and returns the
	 * Expected Return at end of term.
	 * 
	 * @param mean
	 * @param sd
	 * @return double
	 */
	private double project(final double mean, final double sd) {

		double amount = BASE_AMOUNT;

		for (byte i = 0; i < NO_OF_YEARS; i++) {
			final double randomSD = HelperUtil.generateRandomSD(sd);
			final double randomMean = mean + randomSD;
			amount *= ((100 + randomMean) / 100);
		}
		return amount;
	}

}
