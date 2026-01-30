package projekat.ISA.Services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import projekat.ISA.Config.GCounter;
import projekat.ISA.Domain.ViewCount;
import projekat.ISA.Repositories.ViewCountRepository;

@Service
@RequiredArgsConstructor
public class ViewCountService {

    private final GCounterService gCounterService;
    private final ViewCountRepository viewCountRepository;
    private final RestTemplate restTemplate;
    private final String urlBase = "http://localhost:8080/post/exists/";

    @Transactional
    public boolean registerView(Long postId) {
    	//Does post exist?
    	Boolean exists = false;
        try {
            exists = restTemplate.getForObject(urlBase + postId, Boolean.class);
        } catch (Exception e) {
            throw new RuntimeException("Cannot validate post", e);
        }

        if (!exists) {
            return false;
        }
    	//If it does, increment GCounter and write in its own database
        gCounterService.increment(postId);

        long localCount = gCounterService.getCounts()
                .get(postId)
                .getCounts()
                .get(gCounterService.getAppName());

        ViewCount vc = viewCountRepository
                .findById(postId)
                .orElse(new ViewCount(postId, 0L));

        vc.setCount(localCount);
        viewCountRepository.save(vc);
        return true;
    }

    public long getViewsFromDb(Long postId) {
        return viewCountRepository.findById(postId)
                .map(ViewCount::getCount)
                .orElse(0L);
    }
    
    public long getTotalViews(Long postId) {
    	return gCounterService.getCounts()
                .get(postId)
                .getTotal();
    }
}
