package astra.core.servers.balancer;

import astra.core.servers.balancer.elements.LoadBalancerObject;

public interface LoadBalancer<T extends LoadBalancerObject> {

  public T next();
}
