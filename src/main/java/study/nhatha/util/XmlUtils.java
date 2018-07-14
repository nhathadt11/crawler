package study.nhatha.util;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXB;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static study.nhatha.util.ResourceUtils.getResource;

public final class XmlUtils {

  public static ByteArrayOutputStream transform(InputStream source, InputStream rulesIn)
      throws TransformerException {
    TransformerFactory factory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", null);
    Transformer transformer = factory.newTransformer(new StreamSource(rulesIn));

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    transformer.transform(new StreamSource(source), new StreamResult(outputStream));

    return outputStream;
  }

  public static Validator buildValidator(String schemaPath) throws SAXException {
    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    Source schemaFile = new StreamSource(getResource(schemaPath));
    Schema schema = factory.newSchema(schemaFile);

    return schema.newValidator();
  }

  public static <T> T unmarshal(InputStream xmlStreamIn, Class<T> clazz) {
    return JAXB.unmarshal(xmlStreamIn, clazz);
  }

  private XmlUtils() {
  }
}
