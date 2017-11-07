package com.test.impl;

import com.test.CafeLandService;
import com.test.Constants;
import com.test.CrawlerService;
import com.test.business.CategoryManagementRemoteBean;
import com.test.business.NewsManagementRemoteBean;
import com.test.business.ProductManagementRemoteBean;
import com.test.business.SampleHouseManagementRemoteBean;
import com.test.dto.BatDongSanDTO;
import com.test.dto.CategoryDTO;
import com.test.utils.EmailValidator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            Elements searchProductItems = doc.getElementsByClass("list-type-nd").select("li");

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
        String pTitle = searchProductItem.select("h3").select("a").text();
        String href = searchProductItem.select("h3").select("a").attr("href");
        String pThumb = searchProductItem.select("img.imgwith160").attr("src");
        String cityDistrict = searchProductItem.select("div.box-type-3").select("span.bold").text();
        String brief = searchProductItem.getElementsByTag("p").text();
        String priceString = searchProductItem.select("div.box-type-1").select("span.red.bold").text();
        String area = searchProductItem.select("div.box-type-2.pc-block").select("span.bold").text().replace("m2", "");

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
        // batDongSanDTO.setCityDist(cityDistrict.replace("-", ","));
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
        Document doc = Jsoup.connect(fullUrl).userAgent(Constants.userAgent).get();
        String detail = doc.getElementById("sl_mota").html();

        Elements divDacDiemBatDongSan = doc.select("div.menuchildborder").select("div.lopline");
        //logger.info("------- DAC DIEM ----------");
        for(Element dacDiem : divDacDiemBatDongSan){
            String []attrs = dacDiem.select("label").text().split(":");
            if(attrs.length == 2){
                String key = attrs[0].trim();
                String val = attrs[1].trim();
                if(key.equalsIgnoreCase("Mã tài sản")){
                    dto.setCode(val);
                }else if(key.equalsIgnoreCase("Vị trí")){
                    dto.setAddress(val);
                }else if(key.equalsIgnoreCase("Thuộc dự án")){
                    dto.setBrandString(val);
                }else if(key.equalsIgnoreCase("Ngày đăng")){
                    dto.setPostDateStr(val);
                }else if(key.equalsIgnoreCase("Người liên hệ")){
                    dto.setContactName(val);
                }else if(key.equalsIgnoreCase("Địa chỉ")){
                    dto.setContactAddress(val);
                }
            }else if(dacDiem.text().split(":")[0].trim().equalsIgnoreCase("Điện thoại")){
                String phoneNumber = dacDiem.select("a").attr("onclick").split(",")[1].replace("'","").replace(")","").trim();
                dto.setContactPhone(phoneNumber);
            }
        }

        // ma tin dang, loai tin dang
        Elements productType =  doc.select("table.table.table-bordered").select("td");
        for(Element prType : productType){
            String[] text = prType.text().split(":");
            if(text.length == 2){
                String key = text[0].trim();
                String val = text[1].trim();
                if(key.equalsIgnoreCase("Chiều ngang trước")){
                    dto.setWidthSize(val.replace("m", ""));
                }else if(key.equalsIgnoreCase("Chiều dài")){
                    dto.setLongSize(val.replace("m", ""));
                }else if(key.equalsIgnoreCase("Số lầu")){
                    dto.setFloor(val);
                }else if(key.equalsIgnoreCase("Phòng ngủ")){
                    dto.setRoom(val);
                }else if(key.equalsIgnoreCase("Phòng vệ sinh")){
                    dto.setToilet(val);
                }else if(key.equalsIgnoreCase("Chiều ngang")){
                    dto.setWidthSize(val.replace("m", ""));
                }else if(key.equalsIgnoreCase("Chiều dài")){
                    dto.setLongSize(val.replace("m", ""));
                }else if(key.equalsIgnoreCase("Hướng xây dựng")){
                    dto.setDirectionString(val);
                }
            }
        }

        Elements imageElements = doc.getElementById("myCarousel").select("div.carousel-inner").select("div.item").select("img");
        List<String> images = new ArrayList<String>();
        for(Element imageElement: imageElements){
            images.add(imageElement.attr("src"));
        }

        String googleMapUrl = doc.getElementById("menu1").select("iframe").attr("src");
        Map<String, String> vals = getQueryMap(googleMapUrl);
        String[] latLng = vals.get("q").split(",");
        String latitude = latLng[0].trim();
        String longitude = latLng[1].trim();

        String city = doc.getElementById("db_tinhthanh").select("option[selected]").text();
        String district = doc.getElementById("db_quanhuyen").select("option[selected]").text();
        String ward = doc.getElementById("db_phuongxa").select("option[selected]").text();

        dto.setCityDist(district+","+city);
        dto.setWardString(ward);
        dto.setLongitude(longitude);
        dto.setLatitude(latitude);
        dto.setImages(images);
        dto.setDetail(detail);
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

    }

    @Override
    public void crawlerSampleHouse() {

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
}
