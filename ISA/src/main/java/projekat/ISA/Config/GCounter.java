package projekat.ISA.Config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.bucket4j.Bucket;
import lombok.Getter;

@Getter
public class GCounter {

	private Map<String, Long> counts = new ConcurrentHashMap();
	
	public void increment(String id) {
		counts.merge(id, (long) 1, Long::sum);
    }
	
	public void merge(GCounter counters) {
		counters.counts.forEach((replica, value) -> counts.merge(replica, value, Long::max));
	}
	
	public Long getTotal() {
		return counts.values().stream().mapToLong(i -> i).sum();
	}
}
