package projekat.ISA.Config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import io.github.bucket4j.Bucket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GCounter {

	private Map<String, Long> counts = new ConcurrentHashMap();
	
	public void increment(String id, Long increase) {
		counts.merge(id, increase, Long::sum);
    }
	
	public void merge(GCounter counters) {
		counters.counts.forEach((replica, value) -> counts.merge(replica, value, Long::max));
	}
	
	public Long getTotal() {
		return counts.values().stream().mapToLong(i -> i).sum();
	}
}
