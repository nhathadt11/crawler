package study.nhatha.validation;

import org.xml.sax.SAXException;

import java.io.InputStream;

public interface XmlValidationHandler {
  void onPassed(InputStream validXmlContent);
  void onRejected(SAXException e);
}
