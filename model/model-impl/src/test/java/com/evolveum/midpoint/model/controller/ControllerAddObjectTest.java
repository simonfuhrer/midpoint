/*
 * Copyright (c) 2011 Evolveum
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * http://www.opensource.org/licenses/cddl1 or
 * CDDLv1.0.txt file in the source code distribution.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 *
 * Portions Copyrighted 2011 [name of copyright owner]
 */
package com.evolveum.midpoint.model.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.ws.Holder;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.evolveum.midpoint.api.logging.Trace;
import com.evolveum.midpoint.common.jaxb.JAXBUtil;
import com.evolveum.midpoint.common.result.OperationResult;
import com.evolveum.midpoint.common.test.XmlAsserts;
import com.evolveum.midpoint.logging.TraceManager;
import com.evolveum.midpoint.model.test.util.ModelServiceUtil;
import com.evolveum.midpoint.model.test.util.mock.ObjectTypeNameMatcher;
import com.evolveum.midpoint.provisioning.api.ProvisioningService;
import com.evolveum.midpoint.repo.api.RepositoryService;
import com.evolveum.midpoint.schema.exception.ObjectAlreadyExistsException;
import com.evolveum.midpoint.schema.exception.SchemaException;
import com.evolveum.midpoint.xml.ns._public.common.common_1.ObjectContainerType;
import com.evolveum.midpoint.xml.ns._public.common.common_1.OperationResultType;
import com.evolveum.midpoint.xml.ns._public.common.common_1.PropertyReferenceListType;
import com.evolveum.midpoint.xml.ns._public.common.common_1.SystemObjectsType;
import com.evolveum.midpoint.xml.ns._public.common.common_1.UserType;
import com.evolveum.midpoint.xml.schema.SchemaConstants;

/**
 * 
 * @author lazyman
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context-model.xml",
		"classpath:application-context-model-unit-test.xml" })
public class ControllerAddObjectTest {

	private static final File TEST_FOLDER = new File("./src/test/resources/controller/addObject");
	private static final Trace LOGGER = TraceManager.getTrace(ControllerAddObjectTest.class);
	@Autowired(required = true)
	private ModelController controller;
	@Autowired(required = true)
	private RepositoryService repository;
	@Autowired(required = true)
	private ProvisioningService provisioning;

	@Test(expected = IllegalArgumentException.class)
	public void nullObject() throws Exception {
		controller.addObject(null, new OperationResult("Test Operation"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullResult() throws Exception {
		controller.addObject(new UserType(), null);
	}

	@Test(expected = IllegalArgumentException.class)
	@SuppressWarnings("unchecked")
	public void addUserWithoutName() throws Exception {
		final UserType expectedUser = ((JAXBElement<UserType>) JAXBUtil.unmarshal(new File(TEST_FOLDER,
				"add-user-without-name.xml"))).getValue();

		OperationResult result = new OperationResult("Test Operation");
		controller.addObject(expectedUser, result);
		LOGGER.debug(result.debugDump());

		fail("add must fail");
	}

	/**
	 * Testing add user with undefined user template
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void addUserCorrect() throws Exception {
		ModelServiceUtil.mockGetSystemConfiguration(repository, new File(TEST_FOLDER,
				"system-configuration.xml"));

		final UserType expectedUser = ((JAXBElement<UserType>) JAXBUtil.unmarshal(new File(TEST_FOLDER,
				"add-user-correct.xml"))).getValue();

		final String oid = "abababab-abab-abab-abab-000000000001";
		when(
				repository.addObject(argThat(new ObjectTypeNameMatcher(expectedUser.getName())),
						any(OperationResult.class))).thenAnswer(new Answer<String>() {

			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				UserType user = (UserType) invocation.getArguments()[0];
				XmlAsserts.assertPatch(new File(TEST_FOLDER, "add-user-correct.xml"),
						JAXBUtil.marshalWrap(user));

				return oid;
			}
		});

		OperationResult result = new OperationResult("Test Operation");
		String userOid = controller.addObject(expectedUser, result);
		LOGGER.debug(result.debugDump());

		verify(repository, times(1)).addObject(argThat(new ObjectTypeNameMatcher(expectedUser.getName())),
				any(OperationResult.class));
		assertEquals(oid, userOid);
	}
}
