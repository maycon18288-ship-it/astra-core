package astra.core.servers.balancer.type;

import astra.core.servers.balancer.BaseBalancer;
import astra.core.servers.balancer.Server;

import java.util.Comparator;
import java.util.Optional;

public class LeastResponse extends BaseBalancer<Server> {
    
    @Override
    public Server next() {
        if (nextObj.isEmpty()) {
            return null;
        }

        Optional<Server> server = nextObj.stream()
            .filter(Server::canBeSelected)
            .min(Comparator.comparingLong(Server::getLastResponseTime));

        return server.orElse(null);
    }

    @Override
    public int getTotalNumber() {
        return (int) nextObj.stream()
            .filter(Server::canBeSelected)
            .count();
    }
} 