package com.varun.agent.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.varun.agent.config.model.Configuration;
import com.varun.agent.config.model.Directory;
import com.varun.agent.config.model.Host;
import com.varun.agent.config.model.Metadata;
import com.varun.agent.corejavautils.jaxb.JaxBMarshalUnMarshalUtil;

/**
 * Test JAXB generation of agent configuration file using the models created.
 * 
 * @author varunjai
 *
 */
public class TestConfiguration {

	private static final String TEST_AGENT_CONFIG_XML = "src//test//resources//test-agent-config.xml";
	final static Logger logger = LogManager.getLogger(TestConfiguration.class);

	/**
	 * Test the generation of the configuration file of the agent.
	 * 
	 * @throws JAXBException
	 */
	@Test
	public void testConfigMarshall() throws JAXBException {

		final Configuration configuration = new Configuration();
		final Host host = new Host();
		final Host host2 = new Host();
		final Metadata metadata = new Metadata();

		host.setName("voogle-host");
		metadata.setProduct("test");
		metadata.setSubmitterName("voogle_test");
		metadata.setSubmitterEmail("voogle_test@test.com");

		host2.setName("voogle-host3");

		final Directory directory = new Directory();
		final List<String> paths = new ArrayList<>();
		paths.add("D:\\softwares");
		paths.add("D:\\dump");
		directory.setPaths(paths);
		host.setMetadata(metadata);
		host.setDirectory(directory);

		final List<Host> hosts = new ArrayList<>();
		hosts.add(host);
		hosts.add(host2);
		configuration.setHost(hosts);

		JaxBMarshalUnMarshalUtil.marshall(Configuration.class, TEST_AGENT_CONFIG_XML, configuration);

		final File agentConfigFile = new File(TEST_AGENT_CONFIG_XML);
		assert (agentConfigFile.exists());
		assert (agentConfigFile.length() > 0);
	}

	/**
	 * Test un-marshaling of a file containing the agent properties in the
	 * corresponding JAVA object
	 * 
	 * @throws JAXBException
	 */
	@Test
	public void testConfigUnMarshall() throws JAXBException {

		final File agentConfigFile = new File(TEST_AGENT_CONFIG_XML);

		final Configuration configuration = (Configuration) JaxBMarshalUnMarshalUtil.unMarshall(Configuration.class,
				agentConfigFile);

		logger.info("Host name is {}", configuration.getHost().iterator().next().getName());
		assert (configuration != null);
		assert (configuration.getHost() != null);

	}
}
