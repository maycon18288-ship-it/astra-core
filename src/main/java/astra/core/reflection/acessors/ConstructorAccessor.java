package astra.core.reflection.acessors;

import lombok.Getter;

import java.lang.reflect.Constructor;
import java.util.Arrays;

@Getter
public class ConstructorAccessor<T> {

  private final Constructor<T> handle;

  public ConstructorAccessor(Constructor<T> constructor) {
    this(constructor, false);
  }

  public ConstructorAccessor(Constructor<T> constructor, boolean forceAccess) {
    this.handle = constructor;
    if (forceAccess) {
      constructor.setAccessible(true);
    }
  }

  public T newInstance(Object... args) {
    try {
      return this.handle.newInstance(args);
    } catch (ReflectiveOperationException ex) {
      throw new RuntimeException("Cannot invoke constructor.", ex);
    }
  }

  public boolean hasConstructor(Object target) {
    return target != null && this.handle.getDeclaringClass().equals(target.getClass());
  }

    @Override
  public String toString() {
    return "ConstructorAccessor[class=" + this.handle.getDeclaringClass().getName() + ", params=" + Arrays.toString(this.handle.getParameterTypes()) + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }

    if (obj instanceof ConstructorAccessor) {
      ConstructorAccessor<?> other = (ConstructorAccessor<?>) obj;
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
