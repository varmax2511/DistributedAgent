package com.varun.agent.corejavautils.jaxb;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.Validate;

import com.varun.agent.corejavautils.io.CoreFileUtils;

/**
 * Utility class for marshaling and unmarshaling JAXB Elements
 * 
 * @author varunjai
 *
 */
public class JaxBMarshalUnMarshalUtil {

  /**
   * 
   * @param className
   * @param destFilePath
   * @param jaxbElement
   * @throws JAXBException
   */
  public static void marshall(Class<?> className, String destFilePath,
      Object jaxbElement) throws JAXBException {

    final JAXBContext jaxbContext = JAXBContext.newInstance(className);
    final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    final File agentConfigFile = new File(destFilePath);
    jaxbMarshaller.marshal(jaxbElement, agentConfigFile);
  }

  /**
   * 
   * @param className
   * @param sourceFile
   * @return
   * @throws JAXBException
   */
  public static Object unMarshall(Class<?> className, File sourceFile)
      throws JAXBException {

    if (!CoreFileUtils.validateFile(sourceFile)) {
      throw new IllegalArgumentException("Invalid file");
    }

    final JAXBContext jaxbContext = JAXBContext.newInstance(className);
    final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

    return jaxbUnmarshaller.unmarshal(sourceFile);
  }

  /**
   * 
   * @param className
   * @param srcFilePath
   * @return
   * @throws JAXBException
   */
  public static Object unMarshall(Class<?> className, String srcFilePath)
      throws JAXBException {

    Validate.notBlank(srcFilePath);
    final File srcFile = new File(srcFilePath);

    return unMarshall(className, srcFile);
  }
}
