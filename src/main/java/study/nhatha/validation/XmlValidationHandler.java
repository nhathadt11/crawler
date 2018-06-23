package study.nhatha.validation;

import org.xml.sax.SAXException;

public interface XmlValidationHandler {
  void onPassed();
  void onRejected(SAXException e);
}
