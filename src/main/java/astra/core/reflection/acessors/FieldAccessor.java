package astra.core.reflection.acessors;

import java.lang.reflect.Field;
import astra.core.reflection.Accessors;
import lombok.Getter;

@Getter
@SuppressWarnings("unchecked")
public class FieldAccessor<TField> {

  private final Field handle;

  public FieldAccessor(Field field) {
    this(field, false);
  }

  public FieldAccessor(Field field, boolean forceAccess) {
    this.handle = field;
    if (forceAccess) {
      Accessors.setAccessible(field);
    }
  }

  public TField get(Object target) {
    try {
      return (TField) handle.get(target);
    } catch (ReflectiveOperationException ex) {
      throw new RuntimeException("Cannot access field.", ex);
    }
  }

  public void set(Object target, TField value) {
    try {
      handle.set(target, value);
    } catch (ReflectiveOperationException ex) {
      throw new RuntimeException("Cannot access field.", ex);
    }
  }

  public boolean hasField(Object target) {
    return target != null && this.handle.getDeclaringClass().equals(target.getClass());
  }

    @Override
  public String toString() {
    return "FieldAccessor[class=" + this.handle.getDeclaringClass().getName() + ", name=" + this.handle.getName() + ", type=" + this.handle.getType() + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }

    if (obj instanceof FieldAccessor) {
      FieldAccessor<?> other = (FieldAccessor<?>) obj;
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
