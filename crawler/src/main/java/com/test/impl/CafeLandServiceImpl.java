package com.test.impl;

import com.test.CafeLandService;
import com.test.Constants;
import com.test.CrawlerService;
import com.test.business.*;
import com.test.dto.BatDongSanDTO;
import com.test.dto.BrandDTO;
import com.test.dto.CategoryDTO;
import com.test.dto.NewsDTO;
import com.test.utils.EmailValidator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 11/5/2017
 * Time: 4:15 PM
 */
public class CafeLandServiceImpl implements CrawlerService, CafeLandService {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private ProductManagementRemoteBean productManagementBean;
    private CategoryManagementRemoteBean categoryManagementBean;
    private NewsManagementRemoteBean newsManagementBean;
    private SampleHouseManagementRemoteBean sampleHouseManagementBean;
    private BrandManagementRemoteBean brandManagementRemoteBean;

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
        return "https://nhadat.cafeland.vn";
    }

    @Override
    public void doCrawler() throws Exception {
        List<CategoryDTO> categoryDTOs = categoryManagementBean.findAll(true);
        int addNew = 0;
        int exists = 0;
        int error = 0;
        for(CategoryDTO categoryDTO : categoryDTOs){
            for(int i = 1; i < crawlerDeep(); i++){
                if(StringUtils.isNotBlank(categoryDTO.getCafelandUrl())){
                    String orgUrl = categoryDTO.getCafelandUrl();
                    String url = orgUrl + (i > 1 ? "page-" + i + "/" : "");
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

    private List<BatDongSanDTO> gatherInfo(String url){
        List<BatDongSanDTO> items = new ArrayList<BatDongSanDTO>();
        try{
            Document doc = Jsoup.connect(url).timeout(10000).userAgent(Constants.userAgent).get();
            Elements searchProductItems = doc.getElementsByClass("row-item");

            for (Element searchProductItem : searchProductItems) {
                BatDongSanDTO batDongSanDTO = getBrief(searchProductItem);
                items.add(batDongSanDTO);
            }
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return items;
    }

    private BatDongSanDTO getBrief(Element searchProductItem){
        String pTitle = searchProductItem.getElementsByClass("realTitle").text();
        String href = searchProductItem.getElementsByClass("realTitle").attr("href");
        String pThumb = searchProductItem.select("div.images-reales").select("div.img-col").select("img").get(0).attr("data-src").trim();
        String cityDistrict = searchProductItem.select("div.info-location").text();
        String brief = searchProductItem.select("div.reales-preview").text();
        String priceString = searchProductItem.getElementsByClass("reales-price").text();
        String area = searchProductItem.getElementsByClass("reales-area").text().replace("-","").trim();

        BatDongSanDTO batDongSanDTO = new BatDongSanDTO();
        if(searchProductItem.hasClass("line-supervip")){
            batDongSanDTO.setVip(Constants.VIP_1);
        }else if(searchProductItem.hasClass("line-vip1")){
            batDongSanDTO.setVip(Constants.VIP_2);
        }else{
            batDongSanDTO.setVip(Constants.VIP_5);
        }
        batDongSanDTO.setTitle(pTitle);
        batDongSanDTO.setHref(href);
        batDongSanDTO.setThumb(pThumb.trim().replace("'", ""));
        batDongSanDTO.setCityDist(cityDistrict.replace("-", ","));
        Object[] objects = getPriceFromString(priceString);
        batDongSanDTO.setPrice(objects != null ? (Float)objects[0] : -1);
        batDongSanDTO.setUnitString(objects != null ? (String)objects[1]: "");
        batDongSanDTO.setArea(getAreaFromString(area));
        batDongSanDTO.setBrief(brief);
        batDongSanDTO.setPriceString(priceString);

        batDongSanDTO.setSource(crawlerUrl());
        try{
            updateDetail(href, batDongSanDTO);
        }catch (Exception e){
            logger.info(e.getMessage());
        }

        return batDongSanDTO;
    }

    private void updateDetail(String fullUrl, BatDongSanDTO dto) throws Exception{
        try {
            Document doc = Jsoup.connect(fullUrl).timeout(10000).userAgent(Constants.userAgent).get();
            String detail = doc.getElementsByClass("reals-description").select("div.blk-content").html();//.replace("\n", "<br>");
            ;

            Elements dt = doc.getElementsByClass("reals-house-item");
            logger.info("------- DAC DIEM ----------");
            int index = 0;
            for (Element dacDiem : dt) {
                String val = dacDiem.getElementsByClass("value-item").text();
                if(dacDiem.hasClass("opt-mattien")){

                } else if (dacDiem.hasClass("opt-mattien")){
                    dto.setWidthSize(val.replace("m", ""));
                } else if (dacDiem.hasClass("opt-huongnha")){
                    dto.setDirectionString(val);
                } else if (dacDiem.hasClass("opt-sotang")){
                    dto.setFloor(val);
                } else if (dacDiem.hasClass("opt-sotoilet")){
                    dto.setToilet(val);
                } else if (dacDiem.hasClass("opt-duong")){
                    dto.setWidthSize(val.replace("m", ""));
                } else if (dacDiem.hasClass("opt-bancong")){

                } else if (dacDiem.hasClass("opt-sopngu")){
                    dto.setRoom(val);
                } else if (dacDiem.hasClass("opt-phaply")){

                }

                String[] infos = doc.getElementsByClass("reales-location").select("div.col-right").select("div.infor").text().split("/");
                String maTaiSan = infos[0].split(":")[1].trim();
                String ngayCapNhat = infos[1].split(":")[1].trim();
                dto.setCode(maTaiSan);
                dto.setPostDateStr(ngayCapNhat);

                String contactName = doc.getElementsByClass("profile-name").text();
                String contactPhone = doc.getElementsByClass("profile-phone").select("a").attr("onclick").split("'")[1];
                String contactAddr = doc.getElementsByClass("profile-addr").text();
                String contactEmail = doc.getElementsByClass("profile-email").select("a").attr("data-hidden-name") + "@" + doc.getElementsByClass("profile-email").select("a").attr("data-hidden-domain");
                String brand = doc.getElementsByClass("project-suggest").text();
                String brandURL = doc.getElementsByClass("project-suggest").select("a").attr("href");



                dto.setContactMobile(contactPhone);
                dto.setContactName(contactName);
                dto.setContactAddress(contactAddr);
                dto.setContactEmail(contactEmail);
                dto.setBrandString(brand);

                createOrUpdateBrand(brand, brandURL);

                index++;
            }

            List<String> images = new ArrayList<String>();
            Elements elements = doc.getElementsByClass("reals-gallery-thumbnail").select("img.lazyload");
            for (Element imageElement : elements) {
                images.add(imageElement.attr("data-src"));
            }

            String latitude = "0";
            String longitude = "0";
            try {
                Element mapElm = doc.getElementsByClass("frame-map").get(0);
                if (mapElm != null) {
                    String googleMapUrl = mapElm.select("iframe").attr("src");
                    Map<String, String> vals = getQueryMap(googleMapUrl);
                    String[] latLng = vals.get("q").split(",");

                    latitude = latLng[0].trim();
                    longitude = latLng[1].trim();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            String city = "";
            String district = "";
            String ward = "";
            try {
                String[] location = doc.getElementsByClass("infor").get(0).getAllElements().get(2).text().replace(" Lưu tin", "").split(",");
                if (location.length == 3) {
                    city = location[2].trim();
                    district = location[1].trim();
                    ward = location[0].trim();
                }
            }catch (Exception e){
                e.printStackTrace();
            }


            String cityDistr = StringUtils.isNotBlank(district) ? district + ", " + city : city;
            dto.setCityDist(cityDistr);
            dto.setWardString(ward);
            dto.setLongitude(longitude);
            dto.setLatitude(latitude);
            dto.setImages(images);
            dto.setDetail(detail);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new Exception(ex);
        }
    }

    public static Map<String, String> getQueryMap(String query)
    {
        Map<String, String> map = new HashMap<>();
        String[] queryList = query.split("\\?");
        if(queryList.length == 2){
            String[] params = queryList[1].split("&");
            for (String param : params)
            {
                String name = param.split("=")[0];
                String value = param.split("=")[1];
                map.put(name, value);
            }
        }
        return map;
    }

    @Override
    public Integer crawlerDeep() {
        return 5;
    }

    @Override
    public void crawlerNews() {
        String newUrl = "https://nhadat.cafeland.vn/tin-nha-dat";

        try {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            for(int i = 0; i < crawlerDeep(); i++) {
                String url = newUrl + (i > 1 ? "/page-" + i : "");
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

    }

    private List<NewsDTO> gatherNews(String url, SimpleDateFormat df){
        List<NewsDTO> results = new ArrayList<>();
        try{
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
            Document doc = Jsoup.connect(url).userAgent(Constants.userAgent).get();
            Elements news = doc.getElementsByClass("post");

            for(Element newElement : news){
                String thumb = newElement.getElementsByClass("post-image").select("img").attr("src");
                String detailUrl = newElement.getElementsByClass("post-info").select("a").attr("href");
                String title = newElement.getElementsByClass("post-info").select("h3").text();
                String brief = newElement.getElementsByClass("post-info").select("p").text();
                if (!detailUrl.contains("http")) {
                    detailUrl = crawlerUrl() + detailUrl;
                }
                Document detailDoc = Jsoup.connect(detailUrl).userAgent(Constants.userAgent).get();
                String detail = "<div class='summary'><h2>" + brief + "</h2></div>" + detailDoc.getElementsByClass("aloPostContent").html();
                String source = detailDoc.getElementsByClass("aloPostContent").select("a").last().text();
                String strDate = detailDoc.getElementsByClass("tin-author-right").select("p").text();
                Date createdDate = df.parse(strDate.replace("Cập nhật", "").trim());

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
            e.printStackTrace();
            logger.info(e.getMessage());
        }
        return results;
    }

    private Object[] getPriceFromString(String priceStr){
        Float result = -1f;
        if(StringUtils.isNotBlank(priceStr)){
            String[] strs = priceStr.split(" ");
            if(strs.length > 1){
                String val = strs[0].trim();
                if(NumberUtils.isNumber(val)){
                    float price = Float.valueOf(val);
                    if(strs.length == 4){
                        price = Float.valueOf(strs[0].trim() + "." + strs[2].trim());
                    }
                    String unitString = strs[1];

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
                String val = strs[0].trim();
                if(NumberUtils.isNumber(val)){
                    if(val == "0"){
                        return "KXĐ";
                    }
                    return val;
                }
            }
        }
        return result;
    }

    private void createOrUpdateBrand(String brandName, String brandUrl){
        if(StringUtils.isNotBlank(brandUrl)) {
            try {
                System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
                Document doc = Jsoup.connect(brandUrl).userAgent(Constants.userAgent).get();
                String img = doc.getElementsByClass("show-thong-tin-duan").select("div.left-duan").select("img").attr("src");
                List<Element> elements = doc.getElementsByClass("show-thong-tin-duan").select("div.right-duan").select("p");
                BrandDTO dto = new BrandDTO();
                for (int i = 0; i < elements.size(); i++) {
                    Element elm = elements.get(i);
                    if (elm.select("b").text().equals("Diện tích:")) {
                        dto.setArea(elm.text().replace("Diện tích:", ""));
                    } else if (elm.select("b").text().equals("Chủ đầu tự:")) {
                        dto.setOwner(elm.text().replace("Chủ đầu tự:", ""));
                    } else if (elm.select("b").text().equals("Dự kiến bàn giao:")) {
                        dto.setProcess(elm.text().replace("Dự kiến bàn giao:", ""));
                    } else if (elm.select("a").size() > 0) {
                        String detailUrl = elm.select("a").attr("href");
                        doc = Jsoup.connect(detailUrl).userAgent(Constants.userAgent).get();
                        String description = doc.getElementsByClass("sevenPostDes").text();
                        dto.setDescription(description);
                    }
                }

                dto.setBrandName(brandName);
                dto.setThumb(img);
                brandManagementRemoteBean.saveOrUpdate(dto);
            } catch (Exception e) {
                logger.info("------ error: " + brandUrl + " ------");
                logger.info(e.getMessage());
            }
        }
    }

    @Override
    public void crawlerBrand() {
        try{
            String response = Jsoup
                    .connect("https://batdongsan.com.vn/user-management-service/api/v1/User/Login")
                    .data("input", "0367139698", "password", "12345678@Xx", "isRemember", "0")
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)

                    .execute().body();

            //This will get you cookies
            //Map<String, String> cookies = response.cookies();

            //And this is the easieste way I've found to remain in session
           // Document doc = Jsoup.connect("url").cookies(cookies).get();
        }catch (IOException ex){
            ex.printStackTrace();
        }

        /*List<BrandDTO> branchs = brandManagementRemoteBean.findAll();
        for(BrandDTO dto : branchs){

        }*/
    }

    public void setBrandManagementRemoteBean(BrandManagementRemoteBean brandManagementRemoteBean) {
        this.brandManagementRemoteBean = brandManagementRemoteBean;
    }
}
