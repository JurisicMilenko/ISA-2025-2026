package projekat.ISA.Services;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

@Component
public class ActiveUsersService {
    private final ConcurrentHashMap<String, Long> activeUsersMap = new ConcurrentHashMap<>();
    private final AtomicInteger activeUsers;

    public ActiveUsersService(MeterRegistry meterRegistry) {
    	this.activeUsers = new AtomicInteger(0);
    	
    	Gauge.builder("active_users", activeUsers, AtomicInteger::get)
        .description("Number of currently active users")
        .register(meterRegistry);
    }

    public void userLoggedIn(String username, int expiry) {
    	System.out.println("hiiii :3");
    	long now = System.currentTimeMillis();
    	activeUsersMap.put(username, now+(long) expiry);
    	updateGaugeValue();
    }

    @Scheduled(fixedDelay = 10000)
    public void cleanupExpired() {
        long now = System.currentTimeMillis();
        System.out.println(now + " " + activeUsersMap.get("js"));
        activeUsersMap.entrySet().removeIf(entry -> entry.getValue() <= now);
        updateGaugeValue();
    }
    
    public void logout(String username) {
    	System.out.println(activeUsersMap); 
    	activeUsersMap.remove(username);
    	System.out.println(activeUsersMap); 
    	updateGaugeValue();
    }    
    private void updateGaugeValue() {
    	System.out.println(activeUsersMap.size());
        activeUsers.set(activeUsersMap.size());
    }
}