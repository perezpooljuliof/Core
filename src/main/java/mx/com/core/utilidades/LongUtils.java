package mx.com.core.utilidades;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

public class LongUtils {
    public synchronized Long generateUniqueID() {
        long val = -1;
        do {
            val = UUID.randomUUID().getMostSignificantBits();
        } while (val <= 0);
        return val;
    }
}
