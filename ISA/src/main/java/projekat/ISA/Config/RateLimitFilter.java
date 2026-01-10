package projekat.ISA.Config;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.LoadingCache;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


public class RateLimitFilter implements Filter{

	private Bucket createBucket(String ip) {
		return Bucket.builder()
	          .addLimit(Bandwidth.classic(5,Refill.intervally(5, Duration.ofMinutes(1))))
	          .build();
	}	
	
	private final Map<String, Bucket> buckets = new ConcurrentHashMap<String, Bucket>();
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(true);
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String ip = httpRequest.getRemoteAddr();
        
        Bucket bucket = buckets.computeIfAbsent(ip, b -> createBucket(ip));
        
        if (bucket.tryConsume(1)) {
            // the limit is not exceeded
            chain.doFilter(request, response);
        } else {
            // limit is exceeded
            httpResponse.setContentType("text/plain");
            httpResponse.setStatus(429);
            httpResponse.getWriter().append("Too many requests");
        }
		
	}
	
}
