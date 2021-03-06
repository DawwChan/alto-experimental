/*
 * Copyright © 2017 SNLab and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.alto.ext.impl;

import java.util.Collection;
import javax.annotation.Nonnull;
import org.opendaylight.alto.ext.helper.PathManagerHelper;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.DataObjectModification;
import org.opendaylight.controller.md.sal.binding.api.DataTreeChangeListener;
import org.opendaylight.controller.md.sal.binding.api.DataTreeModification;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.Table;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.table.Flow;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PathListener implements DataTreeChangeListener<Flow> {

  private static final Logger LOG = LoggerFactory.getLogger(PathListener.class);
  private static final Short DEFAULT_TABLE_ID = 0;

  private final DataBroker dataBroker;

  public PathListener(DataBroker dataBroker) {
    this.dataBroker = dataBroker;
  }

  @Override
  public void onDataTreeChanged(@Nonnull Collection<DataTreeModification<Flow>> changes) {
    if (changes == null) {
      // This line should never be called since @Nonnull.
      return;
    }
    for (DataTreeModification<Flow> change : changes) {
      if (change == null) {
        continue;
      }
      final InstanceIdentifier<Flow> iid = change.getRootPath().getRootIdentifier();
      if (iid.firstKeyOf(Table.class).getId().equals(DEFAULT_TABLE_ID)) {
        final DataObjectModification<Flow> rootNode = change.getRootNode();
        switch (rootNode.getModificationType()) {
          case WRITE:
            onFlowRuleCreated(iid, rootNode.getDataBefore(), rootNode.getDataAfter());
            break;
          case SUBTREE_MODIFIED:
            onFlowRuleUpdated(iid, rootNode.getDataBefore(), rootNode.getDataAfter());
            break;
          case DELETE:
            onFlowRuleDeleted(iid, rootNode.getDataBefore());
        }
      }
    }
  }

  protected void onFlowRuleCreated(InstanceIdentifier<Flow> iid, Flow dataBefore, Flow dataAfter) {
    if (dataBefore == null) {
      newFlowRule(dataAfter);
      return;
    } else if (PathManagerHelper.isFlowRuleDiff(dataBefore, dataAfter)) {
      updateFlowRule(dataBefore, dataAfter);
    }
    // No changes on this flow rule
    return;
  }

  protected void onFlowRuleUpdated(InstanceIdentifier<Flow> iid, Flow dataBefore, Flow dataAfter) {
    if (PathManagerHelper.isFlowRuleDiff(dataBefore, dataAfter)) {
      updateFlowRule(dataBefore, dataAfter);
    }
    return;
  }

  protected void onFlowRuleDeleted(InstanceIdentifier<Flow> iid, Flow dataBefore) {
    deleteFlowRule(dataBefore);
    return;
  }

  protected void newFlowRule(Flow flow) {
    LOG.info("Flow rule created:\n{}.", flow);
  }

  protected void updateFlowRule(Flow before, Flow after) {
    LOG.info("Flow rule updated:\nFrom: {};\nTo: {}.", before.toString(), after.toString());
  }

  protected void deleteFlowRule(Flow flow) {
    LOG.info("Flow rule deleted:\n{}.", flow);
  }
}
