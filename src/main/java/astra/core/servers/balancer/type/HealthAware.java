package astra.core.servers.balancer.type;

import astra.core.servers.balancer.BaseBalancer;
import astra.core.servers.balancer.Server;

import java.util.Comparator;
import java.util.Optional;
import java.util.Random;

public class HealthAware extends BaseBalancer<Server> {
    private final Random random = new Random();
    private static final double HEALTH_THRESHOLD = 0.3;

    @Override
    public Server next() {
        if (nextObj.isEmpty()) {
            return null;
        }

        // Get servers above health threshold
        Optional<Server> bestServer = nextObj.stream()
            .filter(Server::canBeSelected)
            .filter(server -> server.getHealthScore() > HEALTH_THRESHOLD)
            .max(Comparator.comparingDouble(Server::getHealthScore));

        if (bestServer.isPresent()) {
            // Among healthy servers, randomly select one with probability proportional to health score
            double totalHealth = nextObj.stream()
                .filter(Server::canBeSelected)
                .filter(server -> server.getHealthScore() > HEALTH_THRESHOLD)
                .mapToDouble(Server::getHealthScore)
                .sum();

            if (totalHealth > 0) {
                double rand = random.nextDouble() * totalHealth;
                double cumulative = 0.0;

                for (Server server : nextObj) {
                    if (server.canBeSelected() && server.getHealthScore() > HEALTH_THRESHOLD) {
                        cumulative += server.getHealthScore();
                        if (cumulative >= rand) {
                            return server;
                        }
                    }
                }
            }

            // Fallback to best server if random selection fails
            return bestServer.get();
        }

        // If no servers above threshold, return the best available
        return nextObj.stream()
            .filter(Server::canBeSelected)
            .max(Comparator.comparingDouble(Server::getHealthScore))
            .orElse(null);
    }

    @Override
    public int getTotalNumber() {
        return (int) nextObj.stream()
            .filter(Server::canBeSelected)
            .count();
    }
} 