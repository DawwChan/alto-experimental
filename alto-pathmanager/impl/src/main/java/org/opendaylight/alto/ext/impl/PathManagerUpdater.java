/*
 * Copyright © 2017 SNLab and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.alto.ext.impl;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.yang.gen.v1.urn.opendaylight.alto.ext.pathmanager.rev150105.Path;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.table.Flow;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PathManagerUpdater {

  private final Logger LOG = LoggerFactory.getLogger(PathManagerUpdater.class);
  private final InstanceIdentifier<Path> PATH_LIST_IID = InstanceIdentifier.create(Path.class);
  private final DataBroker dataBroker;

  public PathManagerUpdater(DataBroker dataBroker) {
    this.dataBroker = dataBroker;
  }

  public void newFlowRule(String nodeId, Flow flow) {
    LOG.info("Flow rule of node {} created:\n{}.", nodeId, flow);
  }

  public void updateFlowRule(String nodeId, Flow before, Flow after) {
    LOG.info("Flow rule of node {} updated:\nFrom: {};\nTo: {}.", nodeId, before, after);
  }

  public void deleteFlowRule(String nodeId, Flow flow) {
    LOG.info("Flow rule of node {} deleted:\n{}.", nodeId, flow);
  }
}
