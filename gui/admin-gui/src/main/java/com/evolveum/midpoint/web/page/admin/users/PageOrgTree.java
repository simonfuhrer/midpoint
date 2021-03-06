/*
 * Copyright (c) 2010-2016 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.evolveum.midpoint.web.page.admin.users;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEventSink;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;

import com.evolveum.midpoint.gui.api.model.LoadableModel;
import com.evolveum.midpoint.gui.api.page.PageBase;
import com.evolveum.midpoint.gui.api.util.WebComponentUtil;
import com.evolveum.midpoint.prism.PrismObject;
import com.evolveum.midpoint.prism.polystring.PolyString;
import com.evolveum.midpoint.prism.query.ObjectQuery;
import com.evolveum.midpoint.schema.result.OperationResult;
import com.evolveum.midpoint.schema.util.ObjectQueryUtil;
import com.evolveum.midpoint.security.api.AuthorizationConstants;
import com.evolveum.midpoint.task.api.Task;
import com.evolveum.midpoint.util.logging.LoggingUtils;
import com.evolveum.midpoint.util.logging.Trace;
import com.evolveum.midpoint.util.logging.TraceManager;
import com.evolveum.midpoint.web.application.AuthorizationAction;
import com.evolveum.midpoint.web.application.PageDescriptor;
import com.evolveum.midpoint.web.component.TabbedPanel;
import com.evolveum.midpoint.web.page.admin.orgs.AbstractOrgTabPanel;
import com.evolveum.midpoint.web.page.admin.users.component.TreeTablePanel;
import com.evolveum.midpoint.web.security.MidPointAuthWebSession;
import com.evolveum.midpoint.web.session.SessionStorage;
import com.evolveum.midpoint.xml.ns._public.common.common_3.OrgType;

/**
 * @author lazyman
 */
@PageDescriptor(url = "/admin/org/tree", action = {
        @AuthorizationAction(actionUri = PageAdminUsers.AUTH_ORG_ALL,
                label = PageAdminUsers.AUTH_ORG_ALL_LABEL,
                description = PageAdminUsers.AUTH_ORG_ALL_DESCRIPTION),
        @AuthorizationAction(actionUri = AuthorizationConstants.AUTZ_UI_ORG_TREE_URL,
                label = "PageOrgTree.auth.orgTree.label",
                description = "PageOrgTree.auth.orgTree.description")})
public class PageOrgTree extends PageAdminUsers {

    private static final Trace LOGGER = TraceManager.getTrace(PageOrgTree.class);

    /**
     * todo what is this for? [lazyman]
     */
    public static final String PARAM_ORG_RETURN = "org";

    private static final String DOT_CLASS = PageOrgTree.class.getName() + ".";
    private static final String OPERATION_LOAD_ORG_UNIT = DOT_CLASS + "loadOrgUnit";

    private String ID_TABS = "tabs";
    
    private String ID_ORG_PANEL = "orgPanel";


    public PageOrgTree() {
        initLayout();
    }

    private void initLayout() {
    	AbstractOrgTabPanel tabbedPanel = new AbstractOrgTabPanel(ID_ORG_PANEL, this) {
			
			@Override
			protected Panel createTreePanel(String id, Model<String> model, PageBase pageBase) {
				return new TreeTablePanel(id, model, PageOrgTree.this);
			}
		};

        tabbedPanel.setOutputMarkupId(true);
        add(tabbedPanel);
    }

    public AbstractOrgTabPanel getTabPanel(){
        return (AbstractOrgTabPanel)get(ID_ORG_PANEL);
    }

}
