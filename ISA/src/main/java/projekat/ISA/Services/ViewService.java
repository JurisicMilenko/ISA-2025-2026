package projekat.ISA.Services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import projekat.ISA.Domain.ETL;
import projekat.ISA.Domain.ETLPair;
import projekat.ISA.Domain.Post;
import projekat.ISA.Domain.PostScorePair;
import projekat.ISA.Domain.View;
import projekat.ISA.Repositories.ETLPairRepository;
import projekat.ISA.Repositories.ETLRepository;
import projekat.ISA.Repositories.ViewRepository;

@Service
public class ViewService {
	@Autowired
	private ViewRepository viewRepository;
	@Autowired 
	private ETLRepository etlRepository;
	@Autowired 
	private ETLPairRepository etlPairRepository;
	@Autowired
	private PostService postService;

	public View save(Long videoId) {
		View view = View.builder().date(LocalDateTime.now()).postId(videoId).build();
		ETC();
		return this.viewRepository.save(view);
	}
	
	public List<Post> getTop3(){
		List<Post> posts = new ArrayList<Post>();
		ETL etl = etlRepository.findFirstByOrderByRunDateDesc().get();
		for(ETLPair pair : etl.getScores()) {
			posts.add(pair.getPost());
		}
		
		return posts;
	}
	
	@Scheduled(fixedDelay = 10000)
	public List<PostScorePair> ETC(){
		//extract
		List<View> views = viewRepository.findAll();
		
		//Transform
		//group
		ArrayList<PostScorePair> postScores = new ArrayList();
		long score;
		for(Post post : postService.findAll()) {
			score = 0;
			for(View view : views) {
				if(post.getId() == view.getPostId()) {
					score += Math.max(0, 7-Duration.between(view.getDate(), LocalDateTime.now()).toDays()+1) ;
				}
			}
			postScores.add(new PostScorePair(post, score));
		}
		
		postScores.sort(Comparator.comparing(PostScorePair::getScore).reversed());
		postScores.subList(0,  Math.min(postScores.size(), 3));
		ETL etl = new ETL();
		etl.setRunDate(LocalDateTime.now());
		etlRepository.save(etl);
		List<ETLPair> etlpairs = new ArrayList<ETLPair>();
		for(PostScorePair pair : postScores) {
			ETLPair etlpair = new ETLPair();
			etlpair.setEtl(etl);
			etlpair.setPost(pair.getPost());
			etlpair.setScore(pair.getScore());
			etlpairs.add(etlpair);
			etlPairRepository.save(etlpair);
		}
		etl.setScores(etlpairs);
		etlRepository.save(etl);
		return postScores;
	}
}
