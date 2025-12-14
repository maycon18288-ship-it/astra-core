package astra.core.servers.balancer;

import astra.core.servers.ServerItem;
import astra.core.servers.ServerPing;
import astra.core.servers.balancer.elements.LoadBalancerObject;
import astra.core.servers.balancer.elements.NumberConnection;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class Server implements LoadBalancerObject, NumberConnection {

  private final ServerPing serverPing;
  private final String name;
  private final int max;
  private final AtomicLong lastResponseTime;
  private final AtomicLong lastSuccessfulPing;
  private volatile boolean isHealthy;
  private int consecutiveFailures;
  private static final int MAX_FAILURES = 3;

  public Server(String ip, String name, int max) {
    String[] ipParts = ip.split(":");
    this.serverPing = ServerPing.builder()
        .address(new InetSocketAddress(ipParts[0], Integer.parseInt(ipParts[1])))
        .timeout(2000)
        .maxRetries(3)
        .retryDelay(1000)
        .build();
    this.name = name;
    this.max = max;
    this.lastResponseTime = new AtomicLong(0);
    this.lastSuccessfulPing = new AtomicLong(0);
    this.isHealthy = true;
    this.consecutiveFailures = 0;
  }

  public void fetch() {
    long startTime = System.currentTimeMillis();
    boolean success = false;
    
    try {
      success = serverPing.fetchWithRetry();
      long responseTime = System.currentTimeMillis() - startTime;
      
      lastResponseTime.set(responseTime);
      
      if (success) {
        lastSuccessfulPing.set(System.currentTimeMillis());
        ServerItem.SERVER_COUNT.put(this.name, this.serverPing.getOnline());
        isHealthy = true;
        consecutiveFailures = 0;
      } else {
        consecutiveFailures++;
        if (consecutiveFailures >= MAX_FAILURES) {
          isHealthy = false;
        }
      }
    } catch (Exception e) {
      consecutiveFailures++;
      if (consecutiveFailures >= MAX_FAILURES) {
        isHealthy = false;
      }
    }
  }

  public long getLastResponseTime() {
    return lastResponseTime.get();
  }

  @Override
  public int getActualNumber() {
    return ServerItem.getServerCount(this.name);
  }

  @Override
  public int getMaxNumber() {
    return this.max;
  }

  @Override
  public boolean canBeSelected() {
    boolean canSelect = isHealthy && serverPing.isHealthy() && getActualNumber() < max;
    if (!canSelect) {
    }
    return canSelect;
  }

  public double getHealthScore() {
    if (!isHealthy) return 0.0;
    
    double loadFactor = 1.0 - ((double) getActualNumber() / max);
    double responseFactor = Math.max(0.0, 1.0 - (lastResponseTime.get() / 5000.0));
    
    return (loadFactor * 0.7) + (responseFactor * 0.3);
  }
}
