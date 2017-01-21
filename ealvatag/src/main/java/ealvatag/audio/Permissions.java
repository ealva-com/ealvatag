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

package ealvatag.audio;

//import java.io.IOException;  ==Android==
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.attribute.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Outputs permissions to try and identify why we dont have permissions to read/write file
 */
public class Permissions
{
    public static Logger logger = LoggerFactory.getLogger(Permissions.class);

    // ==Android==
//    /**
//     * Display Permissions
//     *
//     * @param path
//     * @return
//     */
//    public static String displayPermissions(Path path)
//    {
//        if (TagOptionSingleton.getInstance().isAndroid())
//        {
//            return "Android Unknown";
//        }
//        else
//        {
//            StringBuilder sb = new StringBuilder();
//            sb.append("File " + path + " permissions\n");
//            try {
//                {
//                    AclFileAttributeView view = Files.getFileAttributeView(path, AclFileAttributeView.class);
//                    if (view != null) {
//                        sb.append("owner:" + view.getOwner().getName() + "\n");
//                        for (AclEntry acl : view.getAcl()) {
//                            sb.append(acl + "\n");
//                        }
//                    }
//                }
//
//                {
//                    PosixFileAttributeView view = Files.getFileAttributeView(path, PosixFileAttributeView.class);
//                    if (view != null) {
//                        PosixFileAttributes pfa = view.readAttributes();
//                        sb.append(":owner:" +
//                                          pfa.owner().getName() +
//                                          ":group:" +
//                                          pfa.group().getName() +
//                                          ":" +
//                                          PosixFilePermissions.toString(pfa.permissions()) +
//                                          "\n");
//                    }
//                }
//            } catch (IOException ioe) {
//                logger.severe("Unable to read permissions for:" + path.toString());
//            }
//            return sb.toString();
//        }
//    }
}
