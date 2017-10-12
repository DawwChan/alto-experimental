/*
 * Copyright © 2017 SNLab and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.alto.ext.impl;

import java.util.ArrayList;
import java.util.List;
import org.opendaylight.alto.ext.helper.PathManagerHelper;
import org.opendaylight.alto.ext.impl.help.DataStoreHelper;
import org.opendaylight.alto.ext.impl.help.ReadDataFailedException;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.yang.gen.v1.urn.opendaylight.alto.ext.pathmanager.rev150105.PathManager;
import org.opendaylight.yang.gen.v1.urn.opendaylight.alto.ext.pathmanager.rev150105.PathManagerBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.alto.ext.pathmanager.rev150105.path.manager.Path;
import org.opendaylight.yang.gen.v1.urn.opendaylight.alto.ext.pathmanager.rev150105.path.manager.path.FlowDesc;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.table.Flow;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PathManagerUpdater {

  private static final Logger LOG = LoggerFactory.getLogger(PathManagerUpdater.class);
  private final InstanceIdentifier<PathManager> PATH_MANAGER_IID = InstanceIdentifier
      .create(PathManager.class);
  private final DataBroker dataBroker;

  public PathManagerUpdater(DataBroker dataBroker) {
    this.dataBroker = dataBroker;
  }

  public void newFlowRule(String nodeId, Flow flow) {
    LOG.debug("Flow rule of node {} created:\n{}.", nodeId, flow);
    FlowDesc flowDesc = PathManagerHelper.toAltoFlowDesc(flow.getMatch());
    PathManager pathManager = null;
    try {
      pathManager = DataStoreHelper.readOperational(dataBroker, PATH_MANAGER_IID);
    } catch (ReadDataFailedException e) {
      LOG.error("Read data failed: ", e);
    }
    if (pathManager == null) {
      pathManager = new PathManagerBuilder().setPath(new ArrayList<>()).build();
    }
    List<Path> paths = pathManager.getPath();
    paths.sort((a, b) -> b.getId().compareTo(a.getId()));
    for (Path path : paths) {
      LOG.info("Compare flowDesc of path and inserted flow: {} and {}.", flowDesc, path.getFlowDesc());
    }
  }

  public void updateFlowRule(String nodeId, Flow before, Flow after) {
    LOG.debug("Flow rule of node {} updated:\nFrom: {};\nTo: {}.", nodeId, before, after);
  }

  public void deleteFlowRule(String nodeId, Flow flow) {
    LOG.debug("Flow rule of node {} deleted:\n{}.", nodeId, flow);
  }
}
