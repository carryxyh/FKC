/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * StreamHolder
 *
 * @author ziyuan
 * @since 2017-01-10
 */
public class StreamHolder {

    private ByteArrayInputStream bais;

    private ByteArrayOutputStream baos;

    private ObjectInputStream ois;

    private ObjectOutputStream oos;

    private static final int DEFAULT_BUFFER_SIZE = 128;

    public void activeHolder() throws Exception {
        baos = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);
        oos = new ObjectOutputStream(baos);

        bais = new ByteArrayInputStream(baos.toByteArray());
        ois = new ObjectInputStream(bais);
    }

    public void resetHolder() throws Exception {
        if (bais != null) {
            bais.reset();
        }
        if (baos != null) {
            baos.reset();
        }
    }

    /**
     * help gc
     */
    public void destroyHolder() throws Exception {
        if (bais != null) {
            bais.close();
            bais = null;
        }
        if (baos != null) {
            baos.close();
            baos = null;
        }
        if (ois != null) {
            ois.close();
            ois = null;
        }
        if (oos != null) {
            oos.close();
            oos = null;
        }
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }
}
