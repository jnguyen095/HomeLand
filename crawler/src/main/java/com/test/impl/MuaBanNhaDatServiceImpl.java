package com.test.impl;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.test.Constants;
import com.test.CrawlerService;
import com.test.MuaBanNhaDatService;
import com.test.business.CategoryManagementRemoteBean;
import com.test.business.NewsManagementRemoteBean;
import com.test.business.ProductManagementRemoteBean;
import com.test.business.SampleHouseManagementRemoteBean;
import com.test.dto.BatDongSanDTO;
import com.test.dto.CategoryDTO;
import com.test.dto.NewsDTO;
import com.test.dto.SampleHouseDTO;
import com.test.utils.EmailValidator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 8/30/2017
 * Time: 3:18 PM
 */
public class MuaBanNhaDatServiceImpl implements CrawlerService, MuaBanNhaDatService {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private static final Integer PAGE_DEEP = 5;
    private ProductManagementRemoteBean productManagementBean;
    private CategoryManagementRemoteBean categoryManagementBean;
    private NewsManagementRemoteBean newsManagementBean;
    private SampleHouseManagementRemoteBean sampleHouseManagementBean;

    public void setSampleHouseManagementBean(SampleHouseManagementRemoteBean sampleHouseManagementBean) {
        this.sampleHouseManagementBean = sampleHouseManagementBean;
    }

    public void setNewsManagementBean(NewsManagementRemoteBean newsManagementBean) {
        this.newsManagementBean = newsManagementBean;
    }

    public void setCategoryManagementBean(CategoryManagementRemoteBean categoryManagementBean) {
        this.categoryManagementBean = categoryManagementBean;
    }

    public void setProductManagementBean(ProductManagementRemoteBean productManagementBean) {
        this.productManagementBean = productManagementBean;
    }

    @Override
    public String crawlerUrl() {
        return "http://www.muabannhadat.vn";
    }

    @Override
    public Integer crawlerDeep() {
        return 5;
    }

    @Override
    public void doCrawler() throws Exception {
        List<CategoryDTO> categoryDTOs = categoryManagementBean.findAll(true);
        int addNew = 0;
        int exists = 0;
        int error = 0;
        for(CategoryDTO categoryDTO : categoryDTOs){
            for(int i = 1; i < crawlerDeep(); i++){
                if(StringUtils.isNotBlank(categoryDTO.getMuabannhadatUrl())){
                    String url = categoryDTO.getMuabannhadatUrl() + (i > 1 ? "?p=" + i : "");
                    logger.info("--- Gathering: " + url);
                    List<BatDongSanDTO> items = gatherInfo(url);
                    logger.info("--- Processing list: " + items.size());
                    try {
                        Integer[] saveInfos = productManagementBean.saveOrUpdate(categoryDTO.getCategoryId(), items);
                        addNew += saveInfos[0];
                        exists += saveInfos[1];
                        error += saveInfos[2];
                        logger.info("--- Result ----");
                        logger.info("--->> [saved: " + saveInfos[0] + ", exists: " + saveInfos[1] + ", error: " + saveInfos[2] + "] " + url);
                    }catch (Exception e){
                        logger.info(e.getMessage());
                        continue;
                    }
                }
            }
            categoryManagementBean.updateCrawlerStatus(categoryDTO.getCategoryId(), Constants.CRAWLER_DONE);
        }
        productManagementBean.updateCrawlerHistory(crawlerUrl(), addNew, exists, error);
    }

    @Override
    public void crawlerNews() {
        String newUrl = "http://www.muabannhadat.vn/tin-tuc/thong-tin-nha-dat";

        try {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            for(int i = 0; i < crawlerDeep(); i++) {
                String url = newUrl + (i > 1 ? "/?page=" + i : "");
                logger.info("--- Gathering: " + url);
                List<NewsDTO> items = gatherNews(url, df);
                logger.info("--- Processing list: " + items.size());
                try {
                    Integer[] saveInfos = newsManagementBean.saveItems(items);
                    logger.info("--- Result ----");
                    logger.info("--->> [saved: " + saveInfos[0] + ", exists: " + saveInfos[1] + ", error: " + saveInfos[2] + "]");
                }catch (Exception e){
                    logger.info(e.getMessage());
                    continue;
                }
            }

        }catch (Exception e){
            logger.info(e.getMessage());
        }
    }

    @Override
    public void crawlerSampleHouse() {
        String newUrl = "http://www.muabannhadat.vn/tin-tuc/mau-kien-truc-nha-dep";

        try {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            for(int i = 0; i < crawlerDeep(); i++) {
                String url = newUrl + (i > 1 ? "/?page=" + i : "");
                logger.info("--- Gathering: " + url);
                List<SampleHouseDTO> items = gatherSampleHouse(url, df);
                logger.info("--- Processing list: " + items.size());
                try {
                    Integer[] saveInfos = sampleHouseManagementBean.saveItems(items);
                    logger.info("--- Result ----");
                    logger.info("--->> [saved: " + saveInfos[0] + ", exists: " + saveInfos[1] + ", error: " + saveInfos[2] + "]");
                }catch (Exception e){
                    logger.info(e.getMessage());
                    continue;
                }
            }

        }catch (Exception e){
            logger.info(e.getMessage());
        }
    }

    private List<SampleHouseDTO> gatherSampleHouse(String url, SimpleDateFormat df){
        List<SampleHouseDTO> results = new ArrayList<>();
        try{
            Document doc = Jsoup.connect(url).userAgent(Constants.userAgent).get();
            Elements news = doc.getElementsByClass("_CategoeriesListingItem");

            for(Element newElement : news){
                String dateTime = newElement.getElementsByClass("_date").text().trim();
                String thumb = newElement.getElementsByClass("img-thumbnail").attr("src");
                String detailUrl = newElement.getElementsByClass("_ViewDetailCategories").attr("href");
                String title = newElement.getElementsByClass("_title").text();
                String brief = newElement.getElementsByClass("_des").text();
                if (!detailUrl.contains("http")) {
                    detailUrl = crawlerUrl() + detailUrl;
                }
                Document detailDoc = Jsoup.connect(detailUrl).userAgent(Constants.userAgent).get();
                String detail = "<div class='summary'><h2>" + brief + "</h2></div>" + detailDoc.getElementsByClass("__DesFull").html();
                String source = detailDoc.getElementsByTag("p").select(".pull-right").html();
                Date createdDate = df.parse(dateTime);

                SampleHouseDTO dto = new SampleHouseDTO();
                dto.setTitle(title);
                dto.setBrief(brief);
                dto.setThumb(thumb);
                dto.setDescription(detail);
                dto.setView(0);
                dto.setStatus(Constants.ACTIVE);
                dto.setSource(StringUtils.isBlank(source) ? "MuaBanNhaDat" : source);
                dto.setCreatedDate(new Timestamp(createdDate.getTime()));
                dto.setUrl(detailUrl);

                results.add(dto);
            }
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return results;
    }

    private List<NewsDTO> gatherNews(String url, SimpleDateFormat df){
        List<NewsDTO> results = new ArrayList<>();
        try{
            Document doc = Jsoup.connect(url).userAgent(Constants.userAgent).get();
            Elements news = doc.getElementsByClass("_CategoeriesListingItem");

            for(Element newElement : news){
                String dateTime = newElement.getElementsByClass("_date").text().trim();
                String thumb = newElement.getElementsByClass("img-thumbnail").attr("src");
                String detailUrl = newElement.getElementsByClass("_ViewDetailCategories").attr("href");
                String title = newElement.getElementsByClass("_title").text();
                String brief = newElement.getElementsByClass("_des").text();
                if (!detailUrl.contains("http")) {
                    detailUrl = crawlerUrl() + detailUrl;
                }
                Document detailDoc = Jsoup.connect(detailUrl).userAgent(Constants.userAgent).get();
                String detail = "<div class='summary'><h2>" + brief + "</h2></div>" + detailDoc.getElementsByClass("__DesFull").html();
                String source = detailDoc.getElementsByTag("p").select(".pull-right").html();
                Date createdDate = df.parse(dateTime);

                NewsDTO dto = new NewsDTO();
                dto.setTitle(title);
                dto.setBrief(brief);
                dto.setThumb(thumb);
                dto.setDescription(detail);
                dto.setView(0);
                dto.setStatus(Constants.ACTIVE);
                dto.setSource(source);
                dto.setCreatedDate(new Timestamp(createdDate.getTime()));
                dto.setUrl(detailUrl);

                results.add(dto);
            }
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return results;
    }

    private List<BatDongSanDTO> gatherInfo(String url){
        List<BatDongSanDTO> items = new ArrayList<BatDongSanDTO>();
        try{
            Document doc = Jsoup.connect(url).userAgent(Constants.userAgent).get();
            //Elements searchProductItems = doc.getElementsByClass("resultItem");
            Elements searchProductItems = doc.getElementsByClass("listing-item");
            for (Element searchProductItem : searchProductItems) {
                Element aProduct = searchProductItem.select("div.resultItem").get(0);
                BatDongSanDTO batDongSanDTO = getBrief(aProduct);
                batDongSanDTO.setHref(url);
                if(searchProductItem.hasClass("Product-TopListing")){
                    batDongSanDTO.setVip(Constants.VIP_1);
                }else{
                    batDongSanDTO.setVip(Constants.VIP_5);
                }
                items.add(batDongSanDTO);
            }
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return items;
    }

    private BatDongSanDTO getBrief(Element searchProductItem){
        BatDongSanDTO batDongSanDTO = new BatDongSanDTO();
        String pTitle = searchProductItem.select("div.row.title").select("a.title-filter-link").text();
        String href = searchProductItem.select("div.row.title").select("a.title-filter-link").attr("href");
        String brief = searchProductItem.select("div.lline").select("div.col-xs-12.hidden-xs.hidden-sm.cusshort").text();
        String backGroundImg = searchProductItem.select("div.listing-list-img").select("div.image-list").attr("style");
        String pThumb = backGroundImg.substring( backGroundImg.indexOf("http://"), backGroundImg.indexOf(")"));
        String pPrice = searchProductItem.select("div.col-md-3.text-right.listing-price").text();
        String productType = searchProductItem.select("div.listing-list-img").select("div.image-list").select("div.type-product-text").text();

        batDongSanDTO.setTitle(pTitle);
        batDongSanDTO.setHref(crawlerUrl() + href);
        batDongSanDTO.setThumb(pThumb.trim().replace("'", ""));
        batDongSanDTO.setPriceString(pPrice);

        Object[] objects = getPriceFromString(pPrice);
        batDongSanDTO.setPrice(objects != null ? (Float)objects[0] : -1);
        batDongSanDTO.setUnitString(objects != null ? (String)objects[1]: "");
        batDongSanDTO.setBrief(brief);
        batDongSanDTO.setSource(crawlerUrl());
        try{
            updateDetail(crawlerUrl() + href, batDongSanDTO);
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return batDongSanDTO;
    }

    private void updateDetail(String fullUrl, BatDongSanDTO dto) throws Exception{
        Document doc = Jsoup.connect(fullUrl).userAgent(Constants.userAgent).timeout(5000).get();
        String detail = doc.getElementById("Description").html();

        Elements divDacDiemBatDongSan = doc.select("div.row.padding-top-20.main-attribute-area").select("div.row.padding-top-10").select("table.table.table-bordered").get(0).select("tr");
        //logger.info("------- DAC DIEM ----------");
        String area = doc.getElementById("MainContent_ctlDetailBox_lblSurface").text();
        String direction = doc.getElementById("MainContent_ctlDetailBox_lblFengShuiDirection").text();
        String floor = doc.getElementById("MainContent_ctlDetailBox_lblFengShuiDirection").text();
        String room = doc.getElementById("MainContent_ctlDetailBox_lblBedRoom").text();
        String toilet = doc.getElementById("MainContent_ctlDetailBox_lblBathRoom").text();
        String brandString = doc.getElementById("MainContent_ctlDetailBox_lblProject").text();

        dto.setArea(getAreaFromString(area));
        dto.setDirectionString(direction);
        dto.setFloor(floor);
        dto.setRoom(room);
        dto.setToilet(toilet);
        dto.setBrandString(brandString);


        //logger.info("------- LIEN HE ----------");
        Elements divLienHe = doc.select("div.row.detail-contact").select("div.col-xs-12");
        for(Element lienHe : divLienHe){
            if(lienHe.hasClass("name-contact")){
                dto.setContactName(lienHe.text());
            }else if(lienHe.hasClass("address-contact")){
                dto.setContactAddress(lienHe.text());
            }else if(lienHe.hasClass("phone-contact")){
                Elements aElement = lienHe.getElementsByAttribute("data-phoneext");
                String productId = aElement.attr("data-phoneext");
                try {
                /*    WebClient detailPage = new WebClient();
                    detailPage.getCurrentWindow().getEnclosedPage();
                    detailPage.waitForBackgroundJavaScript(5000);
                    detailPage.getOptions().setThrowExceptionOnScriptError(false);
                    detailPage.getOptions().setThrowExceptionOnFailingStatusCode(false);
                    HtmlPage page = detailPage.getPage(fullUrl);
                    HtmlAnchor input = page.getFirstByXPath("//a[@data-phoneext='" + productId + "']");
                    HtmlPage nPage = input.click();
                    ScriptResult result = nPage.executeJavaScript("showphone(this,6185041)");
                    HtmlAnchor ninput = nPage.getFirstByXPath("//a[@data-phoneext='" + productId + "']");
                    dto.setContactPhone(ninput.getTextContent());*/
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                /*Document doc1 = Jsoup.connect("http://www.muabannhadat.vn/Services/Tracking/" + productId + "/GetPhoneCustom")
                        .userAgent(Constants.userAgent)
                        .post();
                dto.setContactPhone(doc1.outerHtml());*/
            }
        }

        // ma tin dang, loai tin dang
        Element productCodeElement =  doc.getElementById("MainContent_ctlDetailBox_lblId");
        dto.setCode(productCodeElement.text());
        Element postDateElement = doc.getElementById("MainContent_ctlDetailBox_lblDateCreated");
        dto.setPostDateStr(postDateElement.text().replace(".", "-"));

        // hinh anh
        Elements imageElements = doc.select("ul.slides").select("li").select("img");
        List<String> images = new ArrayList<String>();
        for(Element imageElement: imageElements){
            images.add(imageElement.attr("src"));
        }

        // Get Longitude, Latitude
        String googleMapLink = doc.getElementById("MainContent_ctlDetailBox_lblMapLink").select("a").attr("href").toString();
        if(StringUtils.isNotBlank(googleMapLink)){
            googleMapLink = googleMapLink.substring(googleMapLink.indexOf("loc:") + "loc:".length());
            String []attrs = googleMapLink.split(",");
            if(attrs != null && attrs.length == 2){
                dto.setLongitude(attrs[1].trim());
                dto.setLatitude(attrs[0].trim());
            }
        }
        String ward = doc.getElementById("MainContent_ctlDetailBox_lblWard").text();
        String district = doc.getElementById("MainContent_ctlDetailBox_lblDistrict").text();
        String city = doc.getElementById("MainContent_ctlDetailBox_lblCity").text();
        String street = doc.getElementById("MainContent_ctlDetailBox_lblStreet").text();

        dto.setAddress((StringUtils.isNotBlank(street) ? street + ", " : "") + (StringUtils.isNotBlank(ward) ? ward + ", " : "") + (StringUtils.isNotBlank(district) ? district + ", " : "") + (StringUtils.isNotBlank(city) ? city + ", " : ""));
        dto.setCityDist(district+','+city);
        dto.setWardString(ward);
        dto.setStreetString(street);
        dto.setImages(images);
        dto.setDetail(removeBadChars(detail));
    }

    private static String removeBadChars(String s) {
        if (s == null) return null;
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<s.length();i++){
            if (Character.isHighSurrogate(s.charAt(i))) continue;
            sb.append(s.charAt(i));
        }
        return sb.toString();
    }

    private Object[] getPriceFromString(String priceStr){
        Float result = -1f;
        if(StringUtils.isNotBlank(priceStr)){
            priceStr = priceStr.replace("VNĐ", "");
            String[] strs = priceStr.split(" ");
            if(strs.length > 1){
                String val = strs[0].trim().replace(",", ".");
                if(NumberUtils.isNumber(val)){
                    float price = Float.valueOf(val);
                    String unitString = strs[1].toLowerCase();
                    return new Object[]{price, unitString};
                }
            }
        }
        return null;
    }

    private String getAreaFromString(String areaStr){
        String result = areaStr;
        if(StringUtils.isNotBlank(areaStr)){
            String[] strs = areaStr.split(" ");
            if(strs.length > 1){
                String val = strs[0];
                if(NumberUtils.isNumber(val)){
                    return val;
                }
            }
        }
        return result;
    }
}
