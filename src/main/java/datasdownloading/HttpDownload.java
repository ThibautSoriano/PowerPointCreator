package main.java.datasdownloading;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import main.java.datasdownloading.entities.Campaign;
import main.java.datasdownloading.entities.CampaignHeader;
import main.java.datasdownloading.entities.PeriodData;
import main.java.excelreader.entities.CampaignRowPeriod;
import main.java.utils.Utils;

public class HttpDownload {

    public static final int BUDAPEST_ID = 44;

    private String userName; // gpapp

    private String password; // Zup38fer

    private String sessionId;

    private XmlReader xmlReader;

    private static HttpClient client;

    static {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(4000).setSocketTimeout(4000)
                .setConnectionRequestTimeout(4000).build();

        client = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig).build();
    }

    public HttpDownload(String userName, String password) throws Exception {

        this.userName = userName;
        this.password = password;

        HttpMessage m = login(this.userName, this.password);

        if (!m.isOk()) {

            throw new LoginException(m.getErrorMessage());
        }
    }

    public HttpDownload() throws Exception {
        this("zburi_owner", "ad12dac");
    }

    private static HttpMessage sendGet(String url) {

        HttpGet request = null;
        try {
            request = new HttpGet(url);
        } catch (IllegalArgumentException e) {
            return new HttpMessage(false,
                    "Only aphanumerical characters allowed", "");
        }

        try {

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            // Read the contents of an entity and return it as a String.
            String content = EntityUtils.toString(entity);

            return new HttpMessage(true, "OK", content);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return new HttpMessage(false,
                    "Connection with the server failed.\nPlease check your internet connection",
                    "");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return new HttpMessage(false, e.getMessage(), "");
        } catch (IOException e) {
            e.printStackTrace();
            return new HttpMessage(false, e.getMessage(), "");
        }

    }

    /**
     * Tries to log in with the distant host gdeapi.gemius.com, if success fills
     * sessionId
     * 
     * @param loginName
     * @param password
     * @return a message containing infos about the success of the login
     */
    private HttpMessage login(String loginName, String password) {

        String url = "https://gdeapi.gemius.com/OpenSession.php?ignoreEmptyParams=Y&login="
                + loginName + "&passwd=" + password;

        HttpMessage m = sendGet(url);

        if (m.isOk()) {

            try {
                sessionId = XmlReader.getSessionID(m.getContent());
            } catch (LoginException e) {
                return new HttpMessage(false, e.getMessage(), "");
            }

        }

        return m;
    }

    public List<CampaignHeader> getCampaignHeaders() {

        HttpMessage m = checkXmlReader();

        if (m.isOk())
            return xmlReader.getAllHeaders();
        else
            return new ArrayList<CampaignHeader>();

    }

    private HttpMessage getCampaignData(String campaignID, String timeDivision,
            String dimensionID) {
        String url = "https://gdeapi.gemius.com/GetBasicStats.php?ignoreEmptyParams=Y&sessionID="
                + sessionId + "&dimensionIDs=" + dimensionID
                + "&indicatorIDs=4%2C28%2C16%2C2%2C30%2C120%2C99&campaignIDs="
                + campaignID + "&timeDivision=" + timeDivision;

        HttpMessage m = sendGet(url);
        if (m.isOk())
            return new HttpMessage(true, "", m.getContent());
        return m;
    }

    private HttpMessage getXmlRankingsDatas(String campaignID,
            String timeDivision) {

        return getCampaignData(campaignID, timeDivision, "1%2C20");
    }

    private HttpMessage getXmlCreativeData(String campaignID,
            String timeDivision) {
        return getCampaignData(campaignID, timeDivision, "1%2C40");
    }

    private HttpMessage getXmlAllData(String campaignID, String timeDivision) {
        return getCampaignData(campaignID, timeDivision, "1");
    }

    private HttpMessage getXmlPlacementList(String campaignID) {
        String url = "https://gdeapi.gemius.com/GetPlacementsList.php?ignoreEmptyParams=Y&sessionID="
                + sessionId + "&campaignID=" + campaignID + "&showPaths=Y";

        HttpMessage m = sendGet(url);

        if (m.isOk())
            return new HttpMessage(true, "", m.getContent());
        return m;
    }

    private HttpMessage getXmlCreativesList(String campaignID) {
        String url = "https://gdeapi.gemius.com/GetCreativesList.php?ignoreEmptyParams=Y&"
                + "sessionID=" + sessionId + "&campaignIDs=" + campaignID;

        HttpMessage m = sendGet(url);

        if (m.isOk())
            return new HttpMessage(true, "", m.getContent());
        return m;
    }

    public Campaign getCampaignRankingsById(String campaignId, boolean general,
            boolean monthly, boolean weekly) {

        HttpMessage m = checkXmlReader();

        if (!m.isOk())
            return null;

        HttpMessage generalData, monthlyData, weeklyData;
        String generalDataStr = "", monthlyDataStr = "", weeklyDataStr = "";

        if (general) {
            generalData = getXmlRankingsDatas(campaignId, "General");
            if (!generalData.isOk())
                return null;
            generalDataStr = generalData.getContent();
        }
        if (monthly) {
            monthlyData = getXmlRankingsDatas(campaignId, "Month");
            if (!monthlyData.isOk())
                return null;
            monthlyDataStr = monthlyData.getContent();
        }
        if (weekly) {
            weeklyData = getXmlRankingsDatas(campaignId, "Week");
            if (!weeklyData.isOk())
                return null;
            weeklyDataStr = weeklyData.getContent();
        }

        HttpMessage placementList = getXmlPlacementList(campaignId);

        if (!placementList.isOk())
            return null;

        HttpMessage wholeTotal = getXmlAllData(campaignId, "General");
        if (!wholeTotal.isOk())
            return null;
        String wholeTotalStr = wholeTotal.getContent();

        try {
            return xmlReader.getCampaign(campaignId, generalDataStr,
                    placementList.getContent(), "", weeklyDataStr,
                    monthlyDataStr, true, wholeTotalStr);
        } catch (LoginException e) {
            // e.printStackTrace();
            login(userName, password);
            return getCampaignRankingsById(campaignId, general, monthly,
                    weekly);
        }

    }

    public Campaign getCampaignTechnicalById(String campaignId) {

        HttpMessage m = checkXmlReader();

        if (!m.isOk())
            return null;

        HttpMessage campaignData = sendGet(
                "https://gdeapi.gemius.com/GetTechStats.php?"
                        + "ignoreEmptyParams=Y&" + "sessionID=" + sessionId
                        + "&dimensionIDs=1&indicatorIDs=4%2C28%2C16%2C2%2C30%2C120%2C99&techDimension=Region&"
                        + "campaignIDs=" + campaignId);

        HttpMessage all = sendGet(
                "https://gdeapi.gemius.com/GetTechStats.php?ignoreEmptyParams=Y&"
                        + "sessionID=" + sessionId
                        + "&dimensionIDs=1&indicatorIDs=4%2C28%2C16%2C2%2C30%2C120%2C99&"
                        + "campaignIDs=" + campaignId
                        + "&techDimension=Country");

        HttpMessage mapIdToCounty = sendGet(
                "https://gdeapi.gemius.com/GetRegionsList.php?ignoreEmptyParams=Y&"
                        + "sessionID=" + sessionId + "&countryID="
                        + BUDAPEST_ID);

        if (campaignData.isOk() && campaignData.isOk()
                && mapIdToCounty.isOk()) {
            try {
                return xmlReader.getCampaignTechnical(campaignId,
                        campaignData.getContent(), all.getContent(),
                        mapIdToCounty.getContent(), BUDAPEST_ID);
            } catch (LoginException e) {

                // e.printStackTrace();
                login(userName, password);
                return getCampaignTechnicalById(campaignId);
            }

        }
        return null;
    }

    public Campaign getCampaignCreativeById(String campaignID, boolean general,
            boolean monthly, boolean weekly) {

        HttpMessage m = checkXmlReader();

        if (!m.isOk())
            return null;

        HttpMessage generalData, monthlyData, weeklyData, wholeTotal;
        String generalDataStr = "", monthlyDataStr = "", weeklyDataStr = "",
                wholeTotalStr = "";

        if (general) {
            generalData = getXmlCreativeData(campaignID, "General");
            if (!generalData.isOk())
                return null;
            generalDataStr = generalData.getContent();
        }
        if (monthly) {
            monthlyData = getXmlCreativeData(campaignID, "Month");
            if (!monthlyData.isOk())
                return null;
            monthlyDataStr = monthlyData.getContent();
        }
        if (weekly) {
            weeklyData = getXmlCreativeData(campaignID, "Week");
            if (!weeklyData.isOk())
                return null;
            weeklyDataStr = weeklyData.getContent();
        }

        wholeTotal = getXmlAllData(campaignID, "General");
        if (!wholeTotal.isOk())
            return null;
        wholeTotalStr = wholeTotal.getContent();

        HttpMessage creativesList = getXmlCreativesList(campaignID);

        if (!creativesList.isOk())
            return null;

        try {
            return xmlReader.getCampaign(campaignID, generalDataStr, "",
                    creativesList.getContent(), weeklyDataStr, monthlyDataStr,
                    false, wholeTotalStr);
        } catch (LoginException e) {
            // e.printStackTrace();
            login(userName, password);
            return getCampaignRankingsById(campaignID, general, monthly,
                    weekly);
        }
    }

    public boolean isSameLogin(HttpDownload other) {
        if (other == null)
            return false;
        return other.client.equals(client) && other.password.equals(password);
    }

    public boolean isSameLogin(String login, String password) {

        return this.userName.equals(login) && this.password.equals(password);
    }

    public void close() {
        String url = "https://gdeapi.gemius.com/CloseSession.php?ignoreEmptyParams=Y&sessionID="
                + sessionId;

        sendGet(url);

    }

    private HttpMessage checkXmlReader() {
        if (xmlReader == null) {
            String url = "https://gdeapi.gemius.com/GetCampaignsList.php?ignoreEmptyParams=Y&sessionID="
                    + sessionId;
            HttpMessage m = sendGet(url);

            if (m.isOk()) {
                try {
                    xmlReader = new XmlReader(m.getContent());
                } catch (LoginException e) {
                    login(userName, password);
                    try {
                        xmlReader = new XmlReader(m.getContent());
                    } catch (LoginException e1) {
                        return new HttpMessage(false, e1.getMessage(), "");
                    }

                }
            } else
                return new HttpMessage(false, m.getErrorMessage(), "");
        }
        return new HttpMessage(true, "", "");
    }

    public PeriodData getPeriodData(String timeDivision, String campaignID) {
        HttpMessage m = checkXmlReader();

        if (!m.isOk())
            return null;

        String xmlAllDataStr = "", xmlWholeTotalStr = "";

        HttpMessage xmlAllData = getXmlAllData(campaignID, timeDivision);
        if (!xmlAllData.isOk())
            return null;
        xmlAllDataStr = xmlAllData.getContent();

        HttpMessage xmlWholeTotal = getXmlAllData(campaignID, "General");
        if (!xmlWholeTotal.isOk())
            return null;
        xmlWholeTotalStr = xmlWholeTotal.getContent();

        // xmlAllData dimension1 timeDivision month ou week
        // xmlWhole dimension 1 timeDivision general

        try {
            return xmlReader.readAllPeriod(xmlAllDataStr, xmlWholeTotalStr);
        } catch (LoginException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public CampaignRowPeriod getAll(String campaignId) {
        HttpMessage m = checkXmlReader();

        if (!m.isOk())
            return null;

        HttpMessage merguez = getXmlAllData(campaignId, "General");

        if (!merguez.isOk())
            return new CampaignRowPeriod();
        try {
            return xmlReader.getAll(merguez.getContent(), false);
        } catch (LoginException e) {
            e.printStackTrace();
            login(userName, password);
            return getAll(campaignId);
        }
    }

    public static void main(String[] args) throws Exception {
        HttpDownload a = new HttpDownload();
        // Campaign c = a.getCampaignTechnicalById("557150106");

        // System.out.println(/c.getCampaignContent());
        // System.out.println(c.getAll());
        HttpMessage m = a.getXmlAllData("557150106", "Week");
        System.out.println(m.getContent());
    }

    public static boolean isInternetConnected() {

        Callable<Boolean> callable1 = new Callable<Boolean>() {
            @Override
            public Boolean call() {
                
                return sendGet("https://www.google.com").isOk();
            }
        };
        Callable<Boolean> callable2 = new Callable<Boolean>() {
            @Override
            public Boolean call() {
               
                return sendGet("https://www.facebook.com").isOk();
            }
        };
        Callable<Boolean> callable3 = new Callable<Boolean>() {
            @Override
            public Boolean call() {
                
                return sendGet("https://www.amazon.com").isOk();
            }
        };

        List<Callable<Boolean>> callableList = new ArrayList<Callable<Boolean>>();
        callableList.add(callable1);
        callableList.add(callable2);
        callableList.add(callable3);

        final ExecutorService service = Executors.newFixedThreadPool(3);

        List<Future<Boolean>> futureObjects = null;
        try {
            futureObjects = service.invokeAll(callableList);
        } catch (InterruptedException e) {
            return false;
        }

        try {
            if (futureObjects.get(0).get() || futureObjects.get(1).get() 
                    || futureObjects.get(2).get()) {
                
                return true;
            
            }
        } catch (InterruptedException | ExecutionException e) {
            
        }
        
        return false;

    }

    public static boolean isGemiusReachable() {
        String url = "https://gdeapi.gemius.com/OpenSession.php?";
        HttpMessage m1 = sendGet(url);
        if (!m1.isOk())
            return false;

        if ("TechnicalBreak".equals(XmlReader.getStatus(m1.getContent())))
            return false;

        return true;
    }

    public static HttpMessage canDownloadDataFromServer() {

        if (!HttpDownload.isInternetConnected())
            return new HttpMessage(false, "No internet connection", "");

        if (!HttpDownload.isGemiusReachable()) {

            String errorMessage = null;
            if (Utils.isGemiusServerMaintenanceHour())
                errorMessage = "gDE system maintance. Try again later";
            else
                errorMessage = "Urgent/sustained gDE system maintance. Please contact technical support";

            return new HttpMessage(false, errorMessage, "");
        }
        return new HttpMessage(true, "", "");
    }

}
