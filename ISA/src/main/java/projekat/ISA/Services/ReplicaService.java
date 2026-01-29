package projekat.ISA.Services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import projekat.ISA.Config.GCounter;

@Service
@EnableScheduling
public class ReplicaService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private GCounter gCounter;

	private final List<String> replicas = List.of("http://localhost:8081", "http://localhost:8082");

	@Scheduled(fixedDelay = 10000)
	public void syncReplicas() {
		Map<String, Long> counts = gCounter.getCounts();
		
		for (String replica : replicas) {
            try {
                restTemplate.postForObject(
                    replica + "/internal/sync",
                    counts,
                    Void.class
                );
            } catch (Exception e) {
            	System.out.println("Nema replike " + replica);
            }
        }
	}
}
