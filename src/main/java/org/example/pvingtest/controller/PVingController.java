package org.example.pvingtest.controller;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.example.pvingtest.common.CommonResponse;
import org.example.pvingtest.model.AnalyzeXmlResp;
import org.example.pvingtest.util.MultipartFileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @package: org.example.pvingtest.demos.web.controller
 * @className: PVingController
 * @author: alexwang
 * @description: TODO
 * @date: 2024/5/26 16:25
 */
@Controller
public class PVingController {

    @GetMapping("/test.html")
    public String index() {
        return "pving-test";
    }

    @PostMapping("/analyzeXml.json")
    @ResponseBody
    public CommonResponse<List<AnalyzeXmlResp>> analyzeXml(@RequestParam("file") MultipartFile file) {

        String contentType = MultipartFileUtil.getFileType(file);

        if (!"application/xml".equals(contentType) && !"text/xml".equals(contentType)) {
            return CommonResponse.fail("文件格式不对,请上传规范的ICSR.xml");
        }

        System.out.println("打印contentType:" + contentType);

        List<AnalyzeXmlResp> respList = parseXml(file);

        return CommonResponse.success(respList);
    }

    private List<AnalyzeXmlResp> parseXml(MultipartFile file) {
        List<AnalyzeXmlResp> respList = new ArrayList<>();

        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(MultipartFileUtil.multipartFileToFile(file));
            Element rootElement = document.getRootElement();

            // 遍历子元素
            for (Element element : (List<Element>) rootElement.elements()) {
                // 获取元素的属性和文本内容
                String elementName = element.getName();
                if ("PORR_IN049016UV".equals(elementName)) {
                    Element controlActProcessElement = element.element("controlActProcess");
                    if (Objects.nonNull(controlActProcessElement) && Objects.nonNull(controlActProcessElement.elements("subject")) && Objects.nonNull(controlActProcessElement.element("subject").element("investigationEvent"))) {
                        AnalyzeXmlResp resp = new AnalyzeXmlResp();
                        resp.setEffectiveTime(controlActProcessElement.element("effectiveTime").attributeValue("value"));
                        boolean needAdd = false;

                        for (Element idElement : (List<Element>) controlActProcessElement.element("subject").element("investigationEvent").elements("id")) {
                            String C11Code = idElement.attributeValue("extension");
                            if ("C.1.1".equals(C11Code)) {
                                String C11Val = idElement.attributeValue("root");
                                resp.setC11(C11Val);
                                needAdd = true;
                            }
                        }

                        Element componentElement = controlActProcessElement.element("subject").element("investigationEvent").element("component");

                        if (Objects.nonNull(componentElement) && Objects.nonNull(componentElement.element("adverseEventAssessment")) && Objects.nonNull(componentElement.element("adverseEventAssessment").element("subject1")) && Objects.nonNull(componentElement.element("adverseEventAssessment").element("subject1").element("primaryRole")) && Objects.nonNull(componentElement.element("adverseEventAssessment").element("subject1").element("primaryRole").element("player1"))) {
                            Element primaryRoleElement = componentElement.element("adverseEventAssessment").element("subject1").element("primaryRole");
                            Element playerElement = componentElement.element("adverseEventAssessment").element("subject1").element("primaryRole").element("player1");
                            if (Objects.nonNull(playerElement) && Objects.nonNull(playerElement.element("name"))) {
                                resp.setD1(playerElement.element("name").getText());
                                needAdd = true;
                            }


                            for (Element subjectOf2Element : (List<Element>) primaryRoleElement.elements("subjectOf2")) {
                                if (Objects.nonNull(subjectOf2Element.element("observation")) && Objects.nonNull(subjectOf2Element.element("observation").element("value"))) {
                                    Element ceElement = subjectOf2Element.element("observation").element("value");
                                    String valType = ceElement.attributeValue("type");
                                    String valCode = ceElement.attributeValue("code");
                                    if ("E.i.2.1b".equals(valCode)) {
                                        System.out.println("valType:" + valType + ",valCode:" + valCode);
                                    }

                                    if ("CE".equals(valType) && "E.i.2.1b".equals(valCode)) {
                                        if (Objects.nonNull(ceElement.element("originalText"))) {
                                            if (StringUtils.hasText(ceElement.element("originalText").getText())) {
                                                needAdd = true;
                                                if (StringUtils.isEmpty(resp.getEi11a())) {
                                                    resp.setEi11a(ceElement.element("originalText").getText());
                                                } else {
                                                    resp.setEi11a(resp.getEi11a() + ", " + ceElement.element("originalText").getText());
                                                }
                                            }
                                        }
                                    }

                                }

                                Element organizerElement = subjectOf2Element.element("organizer");
                                if (Objects.nonNull(organizerElement)) {
                                    for (Element organizerComponentElement : (List<Element>) organizerElement.elements("component")) {
                                        if (Objects.nonNull(organizerComponentElement) && Objects.nonNull(organizerComponentElement.element("substanceAdministration")) && Objects.nonNull(organizerComponentElement.element("substanceAdministration").element("consumable")) && Objects.nonNull(organizerComponentElement.element("substanceAdministration").element("consumable").element("instanceOfKind")) && Objects.nonNull(organizerComponentElement.element("substanceAdministration").element("consumable").element("instanceOfKind").element("kindOfProduct"))) {
                                            Element kindProductCodeElement = organizerComponentElement.element("substanceAdministration").element("consumable").element("instanceOfKind").element("kindOfProduct").element("code");
                                            if (Objects.nonNull(kindProductCodeElement) && ("G.k.2.1.1b".equals(kindProductCodeElement.attributeValue("code")) || "G.k.2.1.2b".equals(kindProductCodeElement.attributeValue("code")))) {
                                                Element kindProductNameElement = organizerComponentElement.element("substanceAdministration").element("consumable").element("instanceOfKind").element("kindOfProduct").element("name");
                                                needAdd = true;
                                                if (StringUtils.isEmpty(resp.getGk22())) {
                                                    resp.setGk22(kindProductNameElement.getText());
                                                } else {
                                                    resp.setGk22(resp.getGk22() + ", " + kindProductNameElement.getText());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (needAdd) {
                            respList.add(resp);
                        }
                    }

                }

            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return respList;
    }
}
