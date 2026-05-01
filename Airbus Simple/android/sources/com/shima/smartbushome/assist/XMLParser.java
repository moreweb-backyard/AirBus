package com.shima.smartbushome.assist;

import android.util.Log;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/* loaded from: classes.dex */
public class XMLParser {
    public Document getDomElement(String str) throws ParserConfigurationException {
        try {
            DocumentBuilder documentBuilderNewDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(str));
            return documentBuilderNewDocumentBuilder.parse(inputSource);
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (ParserConfigurationException e2) {
            Log.e("Error: ", e2.getMessage());
            return null;
        } catch (SAXException e3) {
            Log.e("Error: ", e3.getMessage());
            return null;
        }
    }

    public final String getElementValue(Node node) {
        StringBuilder sb = new StringBuilder();
        if (node.hasChildNodes()) {
            for (Node firstChild = node.getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
                sb.append(firstChild.getNodeValue());
            }
        }
        String string = sb.toString();
        return (string == null || string.length() == 0) ? "" : string;
    }

    public String getValue(Element element, String str) {
        NodeList elementsByTagName = element.getElementsByTagName(str);
        return elementsByTagName.getLength() <= 0 ? "" : getElementValue(elementsByTagName.item(0));
    }
}
