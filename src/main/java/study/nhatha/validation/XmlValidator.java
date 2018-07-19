package study.nhatha.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import study.nhatha.util.XmlUtils;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;

public class XmlValidator {
  private static final Logger LOGGER = LoggerFactory.getLogger(XmlValidator.class);
  private String schemaPath;
  private InputStream xmlSource;
  private XmlValidationHandler xmlValidationHandler;

  public XmlValidator(String schemaPath, InputStream xmlSource, XmlValidationHandler xmlValidationHandler) {
    this.schemaPath = schemaPath;
    this.xmlSource = xmlSource;
    this.xmlValidationHandler = xmlValidationHandler;
  }

  public void validate() throws IOException {
    try {
      Validator validator = XmlUtils.buildValidator(schemaPath);
      validator.validate(new StreamSource(xmlSource));

      xmlSource.reset();
      xmlValidationHandler.onPassed(xmlSource);
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
    } catch (SAXException e) {
      xmlSource.reset();
      xmlValidationHandler.onRejected(e);
    }
  }
}
