package ealvatag.tag.id3.framebody;

import ealvatag.tag.id3.ID3v24Frames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ID3v24FrameBodyTest {

    private final Logger LOG = LoggerFactory.getLogger(ID3v24FrameBody.class);

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
        if (success) {
            LOG.debug("Test was successful.");
        } else {
            LOG.debug("Test was not successful. Errors haven been reported above.");
        }
    }

    private boolean isCompatible(Class<?> superType, Class<?> subType) {
        boolean compatible = true;
        if (!superType.isAssignableFrom(subType)) {
            compatible = false;
            LOG.error(subType.getName() + " does not implement "
                              + superType.getName());
        }
        return compatible;
    }

    public static void main(String[] args) throws Exception {
        new ID3v24FrameBodyTest().testBodyImplementationsAreComplete();
    }

}
