package study.nhatha.util;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public final class XmlUtils {

  public static OutputStream transform(InputStream source, InputStream rulesIn)
      throws TransformerException {
    TransformerFactory factory = TransformerFactory.newInstance();
    Transformer transformer = factory.newTransformer(new StreamSource(rulesIn));

    OutputStream outputStream = new ByteArrayOutputStream();
    transformer.transform(new StreamSource(source), new StreamResult(outputStream));

    return outputStream;
  }

  private XmlUtils() {
  }
}
