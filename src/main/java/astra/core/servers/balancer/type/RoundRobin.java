package astra.core.servers.balancer.type;

import astra.core.servers.balancer.BaseBalancer;
import astra.core.servers.balancer.Server;

import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobin extends BaseBalancer<Server> {
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public Server next() {
        if (nextObj.isEmpty()) {
            return null;
        }

        int attempts = 0;
        int maxAttempts = nextObj.size();
        
        while (attempts < maxAttempts) {
            int index = currentIndex.getAndUpdate(current -> 
                (current + 1) % nextObj.size());
            
            Server server = nextObj.get(index);
            if (server.canBeSelected()) {
                return server;
            }
            attempts++;
        }

        return null;
    }

    @Override
    public int getTotalNumber() {
        return (int) nextObj.stream()
            .filter(Server::canBeSelected)
            .count();
    }
} 