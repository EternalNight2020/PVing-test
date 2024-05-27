package org.example.pvingtest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class PVingTestApplicationTests {

    @Test
    void contextLoads() {
    }

    public void parseXml(String xmlFilePath) {
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(xmlFilePath);
            Element rootElement = document.getRootElement();

            // 遍历子元素
            for (Element element : (List<Element>) rootElement.elements()) {
                // 获取元素的属性和文本内容
                String elementName = element.getName();
                String attributeValue = element.attributeValue("extension");
                String textContent = element.getText();

                System.out.println("elementName:" + elementName);
                // 根据需要处理元素
                //System.out.println(element.getName() + ": " + attributeValue + " - " + textContent);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseTest() {
        parseXml("/Users/kejianwang/Downloads/xml_test_v2/附件1-传输文件示例/ICSR.xml");
    }

}
