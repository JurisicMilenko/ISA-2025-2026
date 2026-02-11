package projekat.ISA.Services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import projekat.ISA.Config.GCounter;

@Service
@Getter
@Setter
public class GCounterService {
	
	private Map<Long, GCounter> counts = new ConcurrentHashMap();
	
	@Value("${spring.application.name}")
    private String appName;

    public void increment(Long postId) {
        counts.computeIfAbsent(postId, id -> new GCounter()).increment(appName, (long) 1);
    }

    public void merge(Map<Long, GCounter> newCount) {
    	newCount.forEach((videoId, newCounts) ->
        counts.merge(
            videoId,
            newCounts,
            (currentC, newC) -> {
                currentC.merge(newC);
                return currentC;
            }
        )
    );
    }
    
    public void initializeFromDb(Long postId, long count) {
        GCounter counter = new GCounter();
        counter.increment(appName, count);
        counts.put(postId, counter);
        System.out.println(postId + " " + appName + " " + count);

    }

}
