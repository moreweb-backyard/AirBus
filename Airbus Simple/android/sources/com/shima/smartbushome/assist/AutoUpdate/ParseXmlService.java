package com.shima.smartbushome.assist.AutoUpdate;

import com.pzj.ipcdemo.utils.DatabaseUtil;
import java.io.InputStream;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: classes.dex */
public class ParseXmlService {
    public HashMap<String, String> parseXml(InputStream inputStream) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        NodeList childNodes = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream).getDocumentElement().getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node nodeItem = childNodes.item(i);
            if (nodeItem.getNodeType() == 1) {
                Element element = (Element) nodeItem;
                if ("version".equals(element.getNodeName())) {
                    map.put("version", element.getFirstChild().getNodeValue());
                } else if (DatabaseUtil.KEY_NAME.equals(element.getNodeName())) {
                    map.put(DatabaseUtil.KEY_NAME, element.getFirstChild().getNodeValue());
                } else if ("url".equals(element.getNodeName())) {
                    map.put("url", element.getFirstChild().getNodeValue());
                }
            }
        }
        return map;
    }
}
