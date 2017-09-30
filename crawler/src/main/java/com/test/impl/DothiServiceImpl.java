package com.test.impl;

import com.test.Constants;
import com.test.CrawlerService;
import com.test.DothiService;
import com.test.business.CategoryManagementRemoteBean;
import com.test.business.NewsManagementRemoteBean;
import com.test.business.ProductManagementRemoteBean;
import com.test.business.SampleHouseManagementRemoteBean;
import com.test.dto.BatDongSanDTO;
import com.test.dto.CategoryDTO;
import com.test.dto.NewsDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import javax.ejb.Stateless;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 9/21/2017
 * Time: 3:50 PM
 */
@Stateless(name = "DothiServiceImpl")
public class DothiServiceImpl implements CrawlerService, DothiService {

    private Logger logger = Logger.getLogger(this.getClass().getName());
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
        return "https://dothi.net";
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
                if(StringUtils.isNotBlank(categoryDTO.getDothiUrl())){
                    String orgUrl = categoryDTO.getDothiUrl();
                    String[] strs = getUrlandExtentionOfUrl(orgUrl);
                    String extention = strs[1];
                    String bUrl = strs[0];
                    String url = bUrl + (i > 1 ? "/p" + i + "." + extention : "." + extention);
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
        String newUrl = "https://dothi.net/tin-thi-truong.htm";

        /*try {
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
        }*/
    }

    @Override
    public void crawlerSampleHouse() {

    }

    private String[] getUrlandExtentionOfUrl(String url){
        int len = url.length();
        int lastIndex = url.lastIndexOf('.');
        String ext = url.substring(lastIndex + 1, len);
        String burl = url.substring(0, lastIndex);
        String []result = new String[2];
        result[0] = burl;
        result[1] = ext;
        return result;
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
                if (!thumb.contains("http")) {
                    thumb = crawlerUrl() + thumb;
                }
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
           // System.setProperty("javax.net.ssl.trustStore", "D:\\dothinet.crt");
            Document doc = Jsoup.connect(url).timeout(10000).userAgent(Constants.userAgent).get();
            Elements searchProductItems = doc.getElementsByClass("listProduct").select("li");

            for (Element searchProductItem : searchProductItems) {
                BatDongSanDTO batDongSanDTO = getBrief(searchProductItem);
                batDongSanDTO.setHref(url);
                items.add(batDongSanDTO);
            }
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return items;
    }

    private BatDongSanDTO getBrief(Element searchProductItem){
        String pTitle = searchProductItem.select("div.desc").select("a").text();
        String href = searchProductItem.select("div.desc").select("a").attr("href");
        String pThumb = searchProductItem.select("img").attr("src");
        String cityDistrict = searchProductItem.select("div.location").text().split(":")[1].trim();

        BatDongSanDTO batDongSanDTO = new BatDongSanDTO();
        if(searchProductItem.select("div.desc").select("a").hasClass("vipdb")){
            batDongSanDTO.setVip(Constants.VIP_1);
        }else if(searchProductItem.select("div.desc").select("a").hasClass("vip1")){
            batDongSanDTO.setVip(Constants.VIP_2);
        }else if(searchProductItem.select("div.desc").select("a").hasClass("vip2")){
            batDongSanDTO.setVip(Constants.VIP_3);
        }else{
            batDongSanDTO.setVip(Constants.VIP_5);
        }
        batDongSanDTO.setTitle(pTitle);
        batDongSanDTO.setHref(crawlerUrl() + href);
        if (!pThumb.contains("http")) {
            pThumb = crawlerUrl() + pThumb;
        }
        batDongSanDTO.setThumb(pThumb.trim().replace("'", ""));
        batDongSanDTO.setCityDist(cityDistrict.replace("-", ","));

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
        String detail = doc.getElementsByClass("pd-desc").html();
        String areaString = doc.getElementById("ContentPlaceHolder1_ProductDetail1_divprice").getElementsByTag("span").get(1).text();
        String pPrice = doc.getElementsByClass("spanprice").text().trim();
        dto.setPriceString(pPrice);
        Object[] objects = getPriceFromString(pPrice);
        dto.setPrice(objects != null ? (Float)objects[0] : -1);
        dto.setUnitString(objects != null ? (String)objects[1]: "");
        dto.setArea(getAreaFromString(areaString));

        String location = doc.getElementsByClass("pd-location").removeClass("spanlocation").text();
        String street = doc.getElementsByClass("pd-location").get(0).children().get(1).text().split("tại")[1].trim();

        // dac diem
        Element tableDacDiem = doc.getElementById("tbl1");
        Elements trDacDiems = tableDacDiem.select("tr");
        for(int i = 0; i < trDacDiems.size(); i++){
            Elements tdDacDiems = trDacDiems.get(i).select("td");
            if(tdDacDiems.size() == 2){
                String key = tdDacDiems.get(0).text().trim();
                String value = tdDacDiems.get(1).text().trim();
                if(key.equalsIgnoreCase("Mã số")){
                    dto.setCode(value);
                }else if(key.equalsIgnoreCase("Ngày đăng tin")){
                    dto.setPostDateStr(value);
                }else if(key.equalsIgnoreCase("Ngày hết hạn")){
                    dto.setExpireDateStr(value);
                }else if(key.equalsIgnoreCase("Hướng nhà")){
                    dto.setDirectionString(value);
                }else if(key.equalsIgnoreCase("Số phòng")){
                    dto.setRoom(value);
                }else if(key.equalsIgnoreCase("Mặt tiền")){
                    dto.setWidthSize(value);
                }else if(key.equalsIgnoreCase("Số tầng")){
                    dto.setFloor(value);
                }else if(key.equalsIgnoreCase("Số toilet")){
                    dto.setToilet(value);
                }else if(key.equalsIgnoreCase("Số tầng")){
                    dto.setFloor(value);
                }
            }
        }

        // dac diem
        Element tableContact = doc.getElementById("tbl2");
        Elements trContacts = tableContact.select("tr");
        for(int i = 0; i < trContacts.size(); i++){
            Elements tdContacts = trContacts.get(i).select("td");
            if(tdContacts.size() == 2){
                String key = tdContacts.get(0).text().trim();
                String value = tdContacts.get(1).text().trim();
                if(key.equalsIgnoreCase("Tên liên lạc")){
                    dto.setContactName(value);
                }else if(key.equalsIgnoreCase("Địa chỉ")){
                    dto.setContactAddress(value);
                }else if(key.equalsIgnoreCase("Di động")){
                    dto.setContactPhone(value);
                }else if(key.equalsIgnoreCase("Email")){
                    dto.setContactEmail(tdContacts.get(1).html());
                }
            }
        }

        // hinh anh
        Elements imageElements = doc.getElementById("myGallery").select("img");
        List<String> images = new ArrayList<String>();
        for(Element imageElement: imageElements){
            images.add(imageElement.attr("src"));
        }

        // Get Longitude, Latitude
        String latitude = doc.getElementById("hddLatitude").val();
        String logitude = doc.getElementById("hddLongtitude").val();
        dto.setLongitude(logitude.trim());
        dto.setLatitude(latitude.trim());

        // String ward = doc.getElementById("MainContent_ctlDetailBox_lblWard").text();
        String district = doc.getElementById("ddlDistrict").text();
        String city = doc.getElementById("ddlCity").text();

        dto.setAddress(location);
        // dto.setCityDist(district+','+city);
        dto.setStreetString(street);
        dto.setImages(images);

        String content = removeBadChars(detail);
        dto.setBrief(subStringAtSpacing(Jsoup.parse(content).text(), 220));
        dto.setDetail(content);
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

    private String subStringAtSpacing(String input, int limit){
        if(input.length() < limit){
            return input;
        }
        String remain = input.substring(limit, input.length());
        int firstSpace = remain.indexOf(' ');
        limit += firstSpace;
        return input.substring(0, limit);
    }

}
