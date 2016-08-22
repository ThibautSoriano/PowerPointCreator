package main.java.datasdownloading;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import main.java.datasdownloading.entities.Campaign;
import main.java.datasdownloading.entities.CampaignHeader;
import main.java.datasdownloading.entities.CampaignStatus;
import main.java.datasdownloading.entities.PeriodData;
import main.java.excelreader.entities.CampaignRow;
import main.java.excelreader.entities.CampaignRowPeriod;
import main.java.utils.Percentage;
import main.java.utils.Utils;

public class XmlReader {

    private Map<String, String> placementsNames = new HashMap<>();

    private Map<String, String> creativesNames = new HashMap<>();

    private Map<String, String> idToCounty = new HashMap<>();

    private List<CampaignHeader> allHeaders = new ArrayList<>();

    public XmlReader(String xmlHeaderDatas) throws LoginException {
        allHeaders = getHeaderList(xmlHeaderDatas);
    }

    public List<CampaignHeader> getAllHeaders() {
        return allHeaders;
    }

    public static String getSessionID(String xmlDatas) throws LoginException {
        String sessionID = "";

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(
                    new ByteArrayInputStream(xmlDatas.getBytes("utf-8"))));

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("OpenSession");

            Node nNode = nList.item(0);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;
                String status = eElement.getElementsByTagName("status").item(0)
                        .getTextContent();
                if ("OK".equals(status)) {
                    sessionID = eElement.getElementsByTagName("sessionID")
                            .item(0).getTextContent();
                } else {
                    throw new LoginException(
                            eElement.getElementsByTagName("errorDescription")
                                    .item(0).getTextContent());
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return sessionID;
    }

    private List<CampaignHeader> getHeaderList(String xmlDatas)
            throws LoginException {
        List<CampaignHeader> headerList = new ArrayList<>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(
                    new ByteArrayInputStream(xmlDatas.getBytes("utf-8"))));

            doc.getDocumentElement().normalize();

            // check if request status was ok
            Node root = doc.getDocumentElement();

            if (root.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) root;
                String status = eElement.getElementsByTagName("status").item(0)
                        .getTextContent();
                if (!"OK".equals(status)) {
                    throw new LoginException(status);
                }
            }

            NodeList nList = doc.getElementsByTagName("campaign");

            for (int i = 0; i < nList.getLength(); i++) {

                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    String campaignID = eElement
                            .getElementsByTagName("campaignID").item(0)
                            .getTextContent();
                    String campaignName = eElement.getElementsByTagName("name")
                            .item(0).getTextContent();
                    String clientName = eElement
                            .getElementsByTagName("clientName").item(0)
                            .getTextContent();
                    CampaignStatus campaignStatus = CampaignStatus
                            .valueOf(eElement.getElementsByTagName("status")
                                    .item(0).getTextContent().toUpperCase());
                    Date creationDate = new Date(Long.parseLong(
                            eElement.getElementsByTagName("creationTS").item(0)
                                    .getTextContent())
                            * 1000);
                    Date startDate = new Date(Long
                            .parseLong(eElement.getElementsByTagName("startTS")
                                    .item(0).getTextContent())
                            * 1000);
                    Date endDate = new Date(Long
                            .parseLong(eElement.getElementsByTagName("endTS")
                                    .item(0).getTextContent())
                            * 1000);

                    headerList.add(new CampaignHeader(campaignID, campaignName,
                            clientName, campaignStatus, creationDate, startDate,
                            endDate));

                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return headerList;
    }

    private void fillMapCreativesNames(String xmlCreatives)
            throws LoginException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(
                    new ByteArrayInputStream(xmlCreatives.getBytes("utf-8"))));

            doc.getDocumentElement().normalize();

            // check if request status was ok
            Node root = doc.getDocumentElement();

            if (root.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) root;
                String status = eElement.getElementsByTagName("status").item(0)
                        .getTextContent();
                if (!"OK".equals(status)) {
                    throw new LoginException(status);
                }
            }

            NodeList nList = doc.getElementsByTagName("creative");

            for (int i = 0; i < nList.getLength(); i++) {

                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    // if
                    // ("/".equals(eElement.getElementsByTagName("placementFullPath").item(0).getTextContent()))
                    // {
                    // placementsNames.put(eElement.getElementsByTagName("placementID").item(0).getTextContent(),
                    // "All");
                    // }
                    // else if
                    // ("N".equals(eElement.getElementsByTagName("isFolder").item(0).getTextContent()))
                    // {
                    creativesNames.put(
                            eElement.getElementsByTagName("creativeID").item(0)
                                    .getTextContent(),
                            eElement.getElementsByTagName("name").item(0)
                                    .getTextContent());
                    // }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private void fillMapPlacementsNames(String xmlPlacementList)
            throws LoginException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder
                    .parse(new InputSource(new ByteArrayInputStream(
                            xmlPlacementList.getBytes("utf-8"))));

            doc.getDocumentElement().normalize();

            // check if request status was ok
            Node root = doc.getDocumentElement();

            if (root.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) root;
                String status = eElement.getElementsByTagName("status").item(0)
                        .getTextContent();
                if (!"OK".equals(status)) {
                    throw new LoginException(status);
                }
            }

            NodeList nList = doc.getElementsByTagName("placement");

            for (int i = 0; i < nList.getLength(); i++) {

                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    if ("/".equals(
                            eElement.getElementsByTagName("placementFullPath")
                                    .item(0).getTextContent())) {
                        // placementsNames.put(eElement.getElementsByTagName("placementID").item(0).getTextContent(),
                        // "All");
                    } else if ("N"
                            .equals(eElement.getElementsByTagName("isFolder")
                                    .item(0).getTextContent())) {
                        placementsNames.put(
                                eElement.getElementsByTagName("placementID")
                                        .item(0).getTextContent(),
                                eElement.getElementsByTagName(
                                        "placementFullPath").item(0)
                                        .getTextContent());
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public Campaign getCampaign(String campaignID, String xmlCampaignDatas,
            String xmlPlacementList, String xmlCreatives, String xmlPeriodWeek,
            String xmlPeriodMonth, boolean ranking, String xmlWholeTotal)
            throws LoginException {

        if (!"".equals(xmlCreatives)) {
            fillMapCreativesNames(xmlCreatives);
        } else if (!"".equals(xmlPlacementList)) {
            fillMapPlacementsNames(xmlPlacementList);
        } else {
            return null;
        }
        Campaign c = new Campaign();

        if (!"".equals(xmlCampaignDatas)) {
            List<CampaignRow> rows = new ArrayList<>();
            CampaignRow all = new CampaignRow();
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder
                        .parse(new InputSource(new ByteArrayInputStream(
                                xmlCampaignDatas.getBytes("utf-8"))));

                doc.getDocumentElement().normalize();
                // check if request status was ok
                Node root = doc.getDocumentElement();

                if (root.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) root;
                    String status = eElement.getElementsByTagName("status")
                            .item(0).getTextContent();
                    if (!"OK".equals(status)) {
                        throw new LoginException(status);
                    }
                }

                NodeList nList = doc.getElementsByTagName("statisticsRecord");

                for (int i = 0; i < nList.getLength(); i++) {

                    Node nNode = nList.item(i);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                        Element eElement = (Element) nNode;
                        boolean exist = false;
                        String firstColumnName = "";
                        if (ranking) {
                            String placementID = eElement
                                    .getElementsByTagName("placementID").item(0)
                                    .getTextContent();
                            if (placementsNames.containsKey(placementID)) {
                                exist = true;
                                firstColumnName = placementsNames
                                        .get(placementID);
                            }
                        } else {
                            String creativeID = eElement
                                    .getElementsByTagName("creativeID").item(0)
                                    .getTextContent();
                            if (creativesNames.containsKey(creativeID)) {
                                exist = true;
                                firstColumnName = creativesNames
                                        .get(creativeID);
                            }
                        }

                        if (exist) {
                            int impressions = Utils.parseInt(
                                    eElement.getElementsByTagName("impressions")
                                            .item(0).getTextContent());
                            int reach = Utils.parseInt(
                                    eElement.getElementsByTagName("reach")
                                            .item(0).getTextContent());
                            float frequency = Utils.parseFloat(
                                    eElement.getElementsByTagName("frequency")
                                            .item(0).getTextContent());
                            int clicks = Utils.parseInt(
                                    eElement.getElementsByTagName("clicks")
                                            .item(0).getTextContent());
                            int userClicks = Utils.parseInt(
                                    eElement.getElementsByTagName("userClicks")
                                            .item(0).getTextContent());
                            float clickThroughRate = Utils.parseFloat(
                                    eElement.getElementsByTagName("CTR").item(0)
                                            .getTextContent());
                            float uniqueCTR = Utils.parseFloat(
                                    eElement.getElementsByTagName("UCTR")
                                            .item(0).getTextContent());

                            CampaignRow currentRow = new CampaignRow(
                                    firstColumnName, impressions, frequency,
                                    clicks, userClicks,
                                    new Percentage(clickThroughRate),
                                    new Percentage(uniqueCTR));
                            currentRow.setReach(reach);
                            if ("All".equals(firstColumnName)) {

                            } else {
                                rows.add(currentRow);
                            }
                        }
                    }
                }
            } catch (ParserConfigurationException | SAXException
                    | IOException e) {
                e.printStackTrace();
            }

            c = new Campaign(getHeaderByID(campaignID), rows,
                    getAll(xmlWholeTotal, false));
        }

        if (!"".equals(xmlPeriodWeek)) {
            c.setWeeklyData(getPeriod(xmlPeriodWeek, ranking, xmlWholeTotal));
        }
        if (!"".equals(xmlPeriodMonth)) {
            c.setMonthlyData(getPeriod(xmlPeriodMonth, ranking, xmlWholeTotal));
        }
        c.setCampaignHeader(getHeaderByID(campaignID));

        return c;
    }

    public CampaignHeader getHeaderByID(String headerID) {
        for (int i = 0; i < allHeaders.size(); i++) {
            if (allHeaders.get(i).getCampaignID().equals(headerID)) {
                return allHeaders.get(i);
            }
        }

        return null;
    }

    public Campaign getCampaignTechnical(String campaignID,
            String xmlCampaignDatas, String xmlSummaryData,
            String xmlIdToCounty, int budapestId) throws LoginException {
        fillMapIdToCounty(xmlIdToCounty);

        Campaign c;
        List<CampaignRow> rows = new ArrayList<>();
        CampaignRow all = new CampaignRow();
        if (!"".equals(xmlCampaignDatas))

            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder
                        .parse(new InputSource(new ByteArrayInputStream(
                                xmlCampaignDatas.getBytes("utf-8"))));

                doc.getDocumentElement().normalize();
                // check if request status was ok
                Node root = doc.getDocumentElement();

                if (root.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) root;
                    String status = eElement.getElementsByTagName("status")
                            .item(0).getTextContent();
                    if (!"OK".equals(status)) {
                        throw new LoginException(status);
                    }
                }

                NodeList nList = doc.getElementsByTagName("statisticsRecord");

                for (int i = 0; i < nList.getLength(); i++) {

                    Node nNode = nList.item(i);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                        Element eElement = (Element) nNode;
                        String countryID = eElement
                                .getElementsByTagName("countryID").item(0)
                                .getTextContent();
                        if ("44".equals(countryID)) {
                            String regionID = eElement
                                    .getElementsByTagName("regionID").item(0)
                                    .getTextContent();
                            if (!"NULL".equals(regionID)) {
                                String regionName = idToCounty.get(regionID);
                                int impressions = Utils
                                        .parseInt(eElement
                                                .getElementsByTagName(
                                                        "impressions")
                                                .item(0).getTextContent());
                                int reach = Utils.parseInt(
                                        eElement.getElementsByTagName("reach")
                                                .item(0).getTextContent());
                                float frequency = Utils
                                        .parseFloat(eElement
                                                .getElementsByTagName(
                                                        "frequency")
                                                .item(0).getTextContent());
                                int clicks = Utils.parseInt(
                                        eElement.getElementsByTagName("clicks")
                                                .item(0).getTextContent());
                                int userClicks = Utils
                                        .parseInt(eElement
                                                .getElementsByTagName(
                                                        "userClicks")
                                                .item(0).getTextContent());
                                float clickThroughRate = Utils.parseFloat(
                                        eElement.getElementsByTagName("CTR")
                                                .item(0).getTextContent());
                                float uniqueCTR = Utils.parseFloat(
                                        eElement.getElementsByTagName("UCTR")
                                                .item(0).getTextContent());

                                CampaignRow currentRow = new CampaignRow(
                                        regionName, impressions, frequency,
                                        clicks, userClicks,
                                        new Percentage(clickThroughRate),
                                        new Percentage(uniqueCTR));
                                currentRow.setReach(reach);
                                rows.add(currentRow);
                            }

                        }
                    }
                }
            } catch (ParserConfigurationException | SAXException
                    | IOException e) {
                e.printStackTrace();
            }

        all = getAll(xmlSummaryData, true);

        c = new Campaign(getHeaderByID(campaignID), rows, all);

        return c;
    }

    public CampaignRowPeriod getAll(String xmlSummaryData,
            boolean withCountryID) throws LoginException {
        CampaignRowPeriod all = new CampaignRowPeriod();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder
                    .parse(new InputSource(new ByteArrayInputStream(
                            xmlSummaryData.getBytes("utf-8"))));

            doc.getDocumentElement().normalize();

            // check if request status was ok
            Node root = doc.getDocumentElement();

            if (root.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) root;
                String status = eElement.getElementsByTagName("status").item(0)
                        .getTextContent();

                if (!"OK".equals(status)) {
                    throw new LoginException(status);
                }
            }

            NodeList nList = doc.getElementsByTagName("statisticsRecord");

            for (int i = 0; i < nList.getLength(); i++) {

                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    boolean exist = true;

                    if (withCountryID) {
                        String countryID = eElement
                                .getElementsByTagName("countryID").item(0)
                                .getTextContent();
                        if (!"44".equals(countryID)) {
                            exist = false;
                        }
                    }

                    if (exist) {
                        int impressions = Utils.parseInt(
                                eElement.getElementsByTagName("impressions")
                                        .item(0).getTextContent());
                        int reach = Utils
                                .parseInt(eElement.getElementsByTagName("reach")
                                        .item(0).getTextContent());
                        float frequency = Utils.parseFloat(
                                eElement.getElementsByTagName("frequency")
                                        .item(0).getTextContent());
                        int clicks = Utils.parseInt(
                                eElement.getElementsByTagName("clicks").item(0)
                                        .getTextContent());
                        int userClicks = Utils.parseInt(
                                eElement.getElementsByTagName("userClicks")
                                        .item(0).getTextContent());
                        float clickThroughRate = Utils
                                .parseFloat(eElement.getElementsByTagName("CTR")
                                        .item(0).getTextContent());
                        float uniqueCTR = Utils.parseFloat(
                                eElement.getElementsByTagName("UCTR").item(0)
                                        .getTextContent());

                        CampaignRowPeriod currentRow = new CampaignRowPeriod(
                                "All", impressions, frequency, clicks,
                                userClicks, new Percentage(clickThroughRate),
                                new Percentage(uniqueCTR), new Date());
                        currentRow.setReach(reach);

                        return currentRow;
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return all;
    }

    private void fillMapIdToCounty(String xmlIdToCounty) throws LoginException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(
                    new ByteArrayInputStream(xmlIdToCounty.getBytes("utf-8"))));

            doc.getDocumentElement().normalize();

            // check if request status was ok
            Node root = doc.getDocumentElement();

            if (root.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) root;
                String status = eElement.getElementsByTagName("status").item(0)
                        .getTextContent();
                if (!"OK".equals(status)) {
                    throw new LoginException(status);
                }
            }

            NodeList nList = doc.getElementsByTagName("region");

            for (int i = 0; i < nList.getLength(); i++) {

                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    idToCounty.put(
                            eElement.getElementsByTagName("regionID").item(0)
                                    .getTextContent(),
                            eElement.getElementsByTagName("name").item(0)
                                    .getTextContent());

                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public PeriodData getPeriod(String xmlPeriodData, boolean ranking,
            String xmlWholeData) throws LoginException {

        List<CampaignRowPeriod> rows = new ArrayList<>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(
                    new ByteArrayInputStream(xmlPeriodData.getBytes("utf-8"))));

            doc.getDocumentElement().normalize();
            // check if request status was ok
            Node root = doc.getDocumentElement();

            if (root.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) root;
                String status = eElement.getElementsByTagName("status").item(0)
                        .getTextContent();
                if (!"OK".equals(status)) {
                    throw new LoginException(status);
                }
            }

            NodeList nList = doc.getElementsByTagName("statisticsRecord");

            for (int i = 0; i < nList.getLength(); i++) {

                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    boolean exist = false;
                    String firstColumnName = "";
                    if (ranking) {
                        String placementID = eElement
                                .getElementsByTagName("placementID").item(0)
                                .getTextContent();
                        if (placementsNames.containsKey(placementID)) {
                            exist = true;
                            firstColumnName = placementsNames.get(placementID);
                        }
                    } else {
                        String creativeID = eElement
                                .getElementsByTagName("creativeID").item(0)
                                .getTextContent();
                        if (creativesNames.containsKey(creativeID)) {
                            exist = true;
                            firstColumnName = creativesNames.get(creativeID);
                        }
                    }
                    if (exist) {
                        Date startPeriod = new Date(Long.parseLong(
                                eElement.getElementsByTagName("period").item(0)
                                        .getTextContent())
                                * 1000);
                        int impressions = Utils.parseInt(
                                eElement.getElementsByTagName("impressions")
                                        .item(0).getTextContent());
                        int reach = Utils
                                .parseInt(eElement.getElementsByTagName("reach")
                                        .item(0).getTextContent());
                        float frequency = Utils.parseFloat(
                                eElement.getElementsByTagName("frequency")
                                        .item(0).getTextContent());
                        int clicks = Utils.parseInt(
                                eElement.getElementsByTagName("clicks").item(0)
                                        .getTextContent());
                        int userClicks = Utils.parseInt(
                                eElement.getElementsByTagName("userClicks")
                                        .item(0).getTextContent());
                        float clickThroughRate = Utils
                                .parseFloat(eElement.getElementsByTagName("CTR")
                                        .item(0).getTextContent());
                        float uniqueCTR = Utils.parseFloat(
                                eElement.getElementsByTagName("UCTR").item(0)
                                        .getTextContent());

                        CampaignRowPeriod currentRow = new CampaignRowPeriod(
                                firstColumnName, impressions, frequency, clicks,
                                userClicks, new Percentage(clickThroughRate),
                                new Percentage(uniqueCTR), startPeriod);
                        currentRow.setReach(reach);
                        if ("All".equals(firstColumnName)) {

                        } else {
                            rows.add(currentRow);
                        }
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        PeriodData p = new PeriodData(rows, getAll(xmlWholeData, false));

        return p;
    }

    // xmlAllData dimension1 timeDivision month ou week
    // xmlWhole dimension 1 timeDivision general
    public PeriodData readAllPeriod(String xmlAllData, String xmlWholeTotal)
            throws LoginException {
        List<CampaignRowPeriod> rows = new ArrayList<>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(
                    new ByteArrayInputStream(xmlAllData.getBytes("utf-8"))));

            doc.getDocumentElement().normalize();
            // check if request status was ok
            Node root = doc.getDocumentElement();

            if (root.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) root;
                String status = eElement.getElementsByTagName("status").item(0)
                        .getTextContent();
                if (!"OK".equals(status)) {
                    throw new LoginException(status);
                }
            }

            NodeList nList = doc.getElementsByTagName("statisticsRecord");

            for (int i = 0; i < nList.getLength(); i++) {

                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    String firstColumnName = "Period summary";

                    Date startPeriod = new Date(Long
                            .parseLong(eElement.getElementsByTagName("period")
                                    .item(0).getTextContent())
                            * 1000);
                    int impressions = Utils.parseInt(
                            eElement.getElementsByTagName("impressions").item(0)
                                    .getTextContent());
                    int reach = Utils
                            .parseInt(eElement.getElementsByTagName("reach")
                                    .item(0).getTextContent());
                    float frequency = Utils.parseFloat(
                            eElement.getElementsByTagName("frequency").item(0)
                                    .getTextContent());
                    int clicks = Utils
                            .parseInt(eElement.getElementsByTagName("clicks")
                                    .item(0).getTextContent());
                    int userClicks = Utils.parseInt(
                            eElement.getElementsByTagName("userClicks").item(0)
                                    .getTextContent());
                    float clickThroughRate = Utils
                            .parseFloat(eElement.getElementsByTagName("CTR")
                                    .item(0).getTextContent());
                    float uniqueCTR = Utils
                            .parseFloat(eElement.getElementsByTagName("UCTR")
                                    .item(0).getTextContent());

                    CampaignRowPeriod currentRow = new CampaignRowPeriod(
                            firstColumnName, impressions, frequency, clicks,
                            userClicks, new Percentage(clickThroughRate),
                            new Percentage(uniqueCTR), startPeriod);
                    currentRow.setReach(reach);

                    rows.add(currentRow);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        PeriodData p = new PeriodData(rows, new CampaignRowPeriod());
        // p.setAll((CampaignRowPeriod) getAll(xmlWholeTotal, false));

        return p;
    }

    public static String getStatus(String xmlDatas) {
        

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(
                    new ByteArrayInputStream(xmlDatas.getBytes("utf-8"))));

            doc.getDocumentElement().normalize();
            Node root = doc.getDocumentElement();

            if (root.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) root;
                String status = eElement.getElementsByTagName("status").item(0)
                        .getTextContent();
                return status;
            }
        } catch (Exception e) {
        }

        return "";

    }

}
