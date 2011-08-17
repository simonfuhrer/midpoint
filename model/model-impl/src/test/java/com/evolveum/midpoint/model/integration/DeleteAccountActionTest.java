/*
 * Copyright (c) 2011 Evolveum
 * 
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * http://www.opensource.org/licenses/cddl1 or CDDLv1.0.txt file in the source
 * code distribution. See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * 
 * Portions Copyrighted 2011 [name of copyright owner]
 */
package com.evolveum.midpoint.model.integration;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.fail;

import java.io.File;
import java.util.Set;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.evolveum.midpoint.common.result.OperationResult;
import com.evolveum.midpoint.model.test.util.ModelTUtil;
import com.evolveum.midpoint.provisioning.api.ResourceObjectChangeListener;
import com.evolveum.midpoint.repo.api.RepositoryService;
import com.evolveum.midpoint.schema.processor.ResourceObject;
import com.evolveum.midpoint.schema.processor.ResourceObjectAttribute;
import com.evolveum.midpoint.schema.processor.ResourceObjectDefinition;
import com.evolveum.midpoint.schema.processor.Schema;
import com.evolveum.midpoint.schema.processor.SchemaProcessorException;
import com.evolveum.midpoint.schema.util.JAXBUtil;
import com.evolveum.midpoint.xml.ns._public.common.common_1.AccountShadowType;
import com.evolveum.midpoint.xml.ns._public.common.common_1.PropertyReferenceListType;
import com.evolveum.midpoint.xml.ns._public.common.common_1.ResourceObjectShadowChangeDescriptionType;
import com.evolveum.midpoint.xml.ns._public.common.common_1.ResourceType;

@ContextConfiguration(locations = { "classpath:application-context-model.xml",
		"classpath:application-context-repository-test.xml", "classpath:application-context-provisioning.xml",
		"classpath:application-context-task.xml" })
public class DeleteAccountActionTest extends AbstractTestNGSpringContextTests {

	@Autowired(required = true)
	private ResourceObjectChangeListener resourceObjectChangeListener;
	@Autowired(required = true)
	private RepositoryService repositoryService;

	// @Autowired(required = true)
	// private ResourceAccessInterface rai;

	@SuppressWarnings("unchecked")
	private ResourceObjectShadowChangeDescriptionType createChangeDescription(String file) throws JAXBException {
		ResourceObjectShadowChangeDescriptionType change = ((JAXBElement<ResourceObjectShadowChangeDescriptionType>) JAXBUtil
				.unmarshal(new File(file))).getValue();
		return change;
	}

	private ResourceObject createSampleResourceObject(ResourceType resourceType, AccountShadowType accountType)
			throws SchemaProcessorException {
		Schema schema = Schema.parse(resourceType.getSchema().getAny().get(0));
		ResourceObjectDefinition rod = (ResourceObjectDefinition) schema.findContainerDefinitionByType(accountType
				.getObjectClass());
		ResourceObject resourceObject = rod.instantiate();

		Set<ResourceObjectAttribute> properties = rod.parseAttributes(accountType.getAttributes().getAny());

		resourceObject.getProperties().addAll(properties);
		return resourceObject;
	}

	// FIXME: fix test
	@Test(enabled = false)
	public void testDeleteAccountAction() throws Exception {
		final String resourceOid = "55555555-d34d-b33f-f00d-333222111111";
		// final String userOid = "87654321-d34d-b33f-f00d-987987987987";
		final String accountOid = "55555555-d34d-b44f-f11d-333222111111";

		// UserType addedUser = null;

		try {
			// create additional change
			ResourceObjectShadowChangeDescriptionType change = createChangeDescription("src/test/resources/account-change-delete-account.xml");
			// adding objects to repo
			final ResourceType resourceType = (ResourceType) ModelTUtil.addObjectToRepo(repositoryService,
					change.getResource());
			final AccountShadowType accountType = (AccountShadowType) ModelTUtil.addObjectToRepo(repositoryService,
					change.getShadow());

			assertNotNull(resourceType);
			// setup provisioning mock
			// ConnectorInstance connector = new ConnectorInstanceIcfImpl();
			// BaseResourceIntegration bri = new
			// BaseResourceIntegration(resourceType);
			// ResourceObject ro = createSampleResourceObject(resourceType,
			// accountType);

			// when(rai.get(any(OperationalResultType.class),
			// any(ResourceObject.class))).thenReturn(ro);
			//
			// when(rai.getConnector()).thenReturn(bri);

			resourceObjectChangeListener.notifyChange(change, new OperationResult("testDeleteAccountAction"));

			try {
				AccountShadowType accountShadow = (AccountShadowType) repositoryService.getObject(accountOid,
						new PropertyReferenceListType(), new OperationResult("Get Object"));
				fail();
			} catch (Exception ex) {
				assertEquals("Object with oid = 55555555-d34d-b44f-f11d-333222111111 not found", ex.getMessage());
			}

		} finally {
			// cleanup repo
			ModelTUtil.deleteObject(repositoryService, accountOid);
			ModelTUtil.deleteObject(repositoryService, resourceOid);
		}
	}
}
