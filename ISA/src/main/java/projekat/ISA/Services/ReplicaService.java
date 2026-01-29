package projekat.ISA.Services;

import java.io.ObjectOutputStream.PutField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import projekat.ISA.Config.GCounter;
import projekat.ISA.Domain.ViewCount;
import projekat.ISA.Repositories.ViewCountRepository;

@Service
@EnableScheduling
public class ReplicaService {

	private RestTemplate restTemplate = new RestTemplate();;

	@Autowired
	private ViewCountRepository viewCountRepository;

	@Autowired
	private GCounterService gCounterService;
	
	@Value("${spring.application.name}")
    private String appName;

	private final List<String> replicas = List.of("http://localhost:8081", "http://localhost:8082");
	
	@PostConstruct
    public void init() {
		List<ViewCount> views = viewCountRepository.findAll();

		for (ViewCount view : views) {
		    gCounterService.initializeFromDb(
		        view.getPostId(),
		        view.getCount()
		    );
		}
    }

	@Scheduled(fixedDelay = 10000)
	public void syncReplicas() {
		Map<Long, GCounter> counts = gCounterService.getCounts();

		for (String replica : replicas) {
			try {
				restTemplate.postForObject(replica + "/internal/sync", counts, Void.class);
				System.out.println(replica + "/internal/sync");
				System.out.println("Uspeh za " + replica);
			} catch (Exception e) {
				System.out.println("Neuspeh za " + replica);
				System.out.println(e.toString());
			}
		}
		gCounterService.setCounts(counts);
		System.out.println(counts);
	}
}
