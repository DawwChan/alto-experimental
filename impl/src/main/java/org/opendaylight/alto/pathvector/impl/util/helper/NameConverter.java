/*
 * Copyright © 2017 Yale University and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.alto.pathvector.impl.util.helper;


public class NameConverter {
    /**
     * @param nodeConnectorId
     * @return node id of nodeConnectorId.
     */
    public static String extractNodeId(String nodeConnectorId) {
        return nodeConnectorId.replaceAll(":[0-9]+$", "");
    }

    /**
     * @param switchId
     * @param outputNodeConnector
     * @return node connector id with switch id and node connector.
     */
    public static String buildNodeConnectorId(String switchId,
                                              String outputNodeConnector) {
        return switchId + ":" + outputNodeConnector;
    }
}
