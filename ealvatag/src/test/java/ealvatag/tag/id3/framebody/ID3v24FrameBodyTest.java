package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v24Frames;
import org.junit.Test;

import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ID3v24FrameBodyTest {

  @Test
  public void testBodyImplementationsAreComplete() throws Exception {
    boolean success = true;
    for (Field field : ID3v24Frames.class.getDeclaredFields()) {
      if (String.class.equals(field.getType())
          && Modifier.isPublic(field.getModifiers())
          && Modifier.isStatic(field.getModifiers())
          && Modifier.isFinal(field.getModifiers())
          && field.getName().startsWith("FRAME_ID")) {
        final String frameID = (String)field.get(null);
        String packageName = ID3v24FrameBody.class.getPackage().getName();
        Class<?> bodyClass
            = Class.forName(packageName + ".FrameBody" + frameID);
        success &= isCompatible(ID3v24FrameBody.class, bodyClass);
      }
    }
    if (!success) {
      fail();
    }
  }

  private boolean isCompatible(Class<?> superType, Class<?> subType) {
    boolean compatible = true;
    if (!superType.isAssignableFrom(subType)) {
      compatible = false;
    }
    return compatible;
  }

  public static void main(String[] args) throws Exception {
    new ID3v24FrameBodyTest().testBodyImplementationsAreComplete();
  }

}
