package astra.core.servers.balancer.type;

import astra.core.servers.balancer.BaseBalancer;
import astra.core.servers.balancer.Server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class WeightedRoundRobin extends BaseBalancer<Server> {
    private final Map<Server, Integer> weights = new HashMap<>();
    private final AtomicInteger currentIndex = new AtomicInteger(0);
    private int gcd = 1;
    private int maxWeight = 0;
    private int currentWeight = 0;

    public void setWeight(Server server, int weight) {
        weights.put(server, weight);
        updateWeights();
    }

    private void updateWeights() {
        maxWeight = weights.values().stream()
            .mapToInt(Integer::intValue)
            .max()
            .orElse(1);

        gcd = weights.values().stream()
            .mapToInt(Integer::intValue)
            .reduce(this::gcd)
            .orElse(1);
    }

    private int gcd(int a, int b) {
        while (b > 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    @Override
    public Server next() {
        if (nextObj.isEmpty()) {
            return null;
        }

        int count = 0;
        while (count < nextObj.size()) {
            currentIndex.set((currentIndex.get() + 1) % nextObj.size());
            if (currentIndex.get() == 0) {
                currentWeight = currentWeight - gcd;
                if (currentWeight <= 0) {
                    currentWeight = maxWeight;
                    if (currentWeight == 0) {
                        return null;
                    }
                }
            }

            Server server = nextObj.get(currentIndex.get());
            if (server.canBeSelected()) {
                int weight = weights.getOrDefault(server, 1);
                if (weight >= currentWeight) {
                    return server;
                }
            }
            count++;
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