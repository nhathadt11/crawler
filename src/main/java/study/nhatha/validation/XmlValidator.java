package study.nhatha.validation;

import org.xml.sax.SAXException;
import study.nhatha.util.XmlUtils;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;

public class XmlValidator {
  private String schemaPath;
  private InputStream xmlSource;
  private XmlValidationHandler xmlValidationHandler;

  public XmlValidator(String schemaPath, InputStream xmlSource, XmlValidationHandler xmlValidationHandler) {
    this.schemaPath = schemaPath;
    this.xmlSource = xmlSource;
    this.xmlValidationHandler = xmlValidationHandler;
  }

  public void validate() {
    try {
      Validator validator = XmlUtils.buildValidator(schemaPath);
      validator.validate(new StreamSource(xmlSource));

      xmlSource.reset();
      xmlValidationHandler.onPassed();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      xmlValidationHandler.onRejected(e);
    }
  }
}
