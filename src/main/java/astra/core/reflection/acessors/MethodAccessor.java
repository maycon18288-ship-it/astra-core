package astra.core.reflection.acessors;

import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public class MethodAccessor {

  private final Method handle;

  public MethodAccessor(Method method) {
    this(method, false);
  }

  public MethodAccessor(Method method, boolean forceAccess) {
    this.handle = method;
    if (forceAccess) {
      method.setAccessible(true);
    }
  }

  public Object invoke(Object target, Object... args) {
    try {
      return handle.invoke(target, args);
    } catch (ReflectiveOperationException ex) {
      throw new RuntimeException("Cannot invoke method.", ex);
    }
  }

  public boolean hasMethod(Object target) {
    return target != null && this.handle.getDeclaringClass().equals(target.getClass());
  }

    @Override
  public String toString() {
    return "MethodAccessor[class=" + this.handle.getDeclaringClass().getName() + ", name=" + this.handle.getName() + ", params=" + this.handle.getParameterTypes().toString() + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }

    if (obj instanceof MethodAccessor) {
      MethodAccessor other = (MethodAccessor) obj;
      if (other.handle.equals(handle)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public int hashCode() {
    return this.handle.hashCode();
  }
}
