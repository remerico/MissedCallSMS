package events;

import com.squareup.otto.Bus;

public class EventBus {
	
	private static Bus BUS = new Bus();

	private EventBus() { }
	
	public static Bus getInstance() {
		return BUS;
	}
	
}
