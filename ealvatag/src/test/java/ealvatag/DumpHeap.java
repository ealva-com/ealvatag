/*
 * Copyright (c) 2017 Eric A. Snell
 *
 * This file is part of eAlvaTag.
 *
 * eAlvaTag is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * eAlvaTag is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with eAlvaTag.  If not,
 * see <http://www.gnu.org/licenses/>.
 */

package ealvatag;

import com.sun.management.HotSpotDiagnosticMXBean;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.AudioHeader;
import ealvatag.tag.Tag;

import javax.management.MBeanServer;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * Created by Eric A. Snell on 1/17/17.
 */

public class DumpHeap {
    private static final String HOTSPOT_BEAN_NAME = "com.sun.management:type=HotSpotDiagnostic";

    private static volatile HotSpotDiagnosticMXBean hotspotMBean;

    private static void dumpHeap(String fileName, boolean live) {
        initHotspotMBean();
        try {
            hotspotMBean.dumpHeap(fileName, live);
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    private static void initHotspotMBean() {
        if (hotspotMBean == null) {
            synchronized (DumpHeap.class) {
                if (hotspotMBean == null) {
                    hotspotMBean = getHotspotMBean();
                }
            }
        }
    }

    private static HotSpotDiagnosticMXBean getHotspotMBean() {
        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            return ManagementFactory.newPlatformMXBeanProxy(server, HOTSPOT_BEAN_NAME, HotSpotDiagnosticMXBean.class);
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    public static void main(String[] args) throws Exception{
        final SimpleDateFormat format = new SimpleDateFormat("-dd-MM-yy:HH:mm:SS");
        String heapDumpFileName = "heap" + format.format(new Date()) +  ".bin";

        File inputFile = new File("ealvatag/testdata", "issue52.mp3");
        if (inputFile.exists()) {
            AudioFile audioFile = AudioFileIO.read(inputFile);
            final AudioHeader audioHeader = audioFile.getAudioHeader();
            final Tag tag = audioFile.getTag();
            System.out.println(audioHeader);
            System.out.println("Fields:" + Integer.toString(tag.getFieldCount()));

            dumpHeap(heapDumpFileName, true);
        } else {
            System.err.println(inputFile.getCanonicalPath() + " not found");
        }
    }
}