package main1.java1;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.List;
import java.util.Random;

public class HelperUtil {

	private static final Random RANDOM = new Random();
	
	/**
	 * Generates a random SD value using the 68-95-99.7 rule. The value can be positive or negative.
	 * @param oneSD
	 * @return
	 */
	public static double generateRandomSD(final double oneSD) {

		final double sigmaVar = RANDOM.nextDouble();
		final double sd;

		if (sigmaVar <= 0.6827) {
			sd = RANDOM.nextDouble() * oneSD;
		} else if (sigmaVar <= 0.9545) {
			sd = 2 * RANDOM.nextDouble() * oneSD;
		} else if (sigmaVar <= 0.9973) {
			sd = 3 * RANDOM.nextDouble() * oneSD;
		} else {
			// for anything more than 3 sigma, treat as 4
			sd = 4 * RANDOM.nextDouble() * oneSD;
		}

		final boolean isPositive = RANDOM.nextBoolean();
		final double randomSD = isPositive ? sd : sd * -1;
		return randomSD;
	}

	/**
	 * Calculate inflation adjusted for what the passed amount would be noOfYears ago.
	 * @param inflationPercentage
	 * @param noOfYears
	 * @param amount
	 * @return double
	 */
	public static double adjustBackwardInflation(final double inflationPercentage, final int noOfYears, final double amount) {

		final double interimDivisor = Math.pow((100 + inflationPercentage) / 100, noOfYears);
		final double inflationAdjustedAmount = amount / interimDivisor;
		return inflationAdjustedAmount;
	}

	/**
	 * Rounds double amount using Banker's rounding method.
	 * @param amount
	 * @return double
	 */
	public static double round(final double amount) {

		return new BigDecimal(amount).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}

	/**
	 * Returns median from the list based on even/odd size of list.
	 * @param list
	 * @return double
	 */
	public static double calculateMedian(final List<Double> list) {

		final int size = list.size();
		final double median;
		if (size % 2 == 0) {
			final int almostMiddleIndex = size / 2;
			median = (list.get(almostMiddleIndex) + list.get(almostMiddleIndex - 1)) / 2;
		} else {
			final int middleIndex = size / 2;
			median = list.get(middleIndex);
		}
		return median;
	}

	/**
	 * Calculates index for a list for a particular percentile. NOTE - This is a simple implementation supporting sizes of multiple of 100s.
	 * @param percentile
	 * @param size
	 * @return int
	 */
	public static int calculatePercentileIndex(final int percentile, final int size) {

		return (size * percentile) / 100;
	}

	/**
	 * Pretty prints messages using {@link MessageFormat#format(String, Object...)}. Written for brevity.
	 * @param message
	 * @param arguments
	 */
	public static void print(final String message, final Object... arguments) {

		System.out.println(MessageFormat.format(message, arguments));
	}



}
