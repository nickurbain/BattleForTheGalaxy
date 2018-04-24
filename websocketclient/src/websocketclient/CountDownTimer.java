package websocketclient;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Simple countdown timer demo of java.util.Timer facility.
 */
public class CountDownTimer {

	public static void main(final String args[]) {
//		if (args.length != 1) {
//			System.err.println("Usage: java Countdown <time in secs>");
//			System.exit(1);
//		}
		final Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			int i = 180; // In seconds 

			public void run() {
				System.out.println(i--);
				if (i < 0)
					timer.cancel();
			}
		}, 0, 1000);
	}
}
