package projekat.ISA.Services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import projekat.ISA.Config.GCounter;
import projekat.ISA.Domain.ViewCount;
import projekat.ISA.Repositories.ViewCountRepository;

@Service
@RequiredArgsConstructor
public class ViewCountService {

    private final GCounterService gCounterService;
    private final ViewCountRepository viewCountRepository;

    @Transactional
    public void registerView(Long postId) {
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
