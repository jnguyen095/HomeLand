package com.test.impl;

import com.test.BatDongSanService;
import com.test.Constants;
import com.test.business.CategoryManagementRemoteBean;
import com.test.business.ProductManagementRemoteBean;
import com.test.dto.BatDongSanDTO;
import com.test.dto.CategoryDTO;
import com.test.dto.CategoryTreeDTO;
import com.test.utils.EmailValidator;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 7/10/15
 * Time: 11:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "BatDongSanServiceImpl")
public class BatDongSanServiceImpl implements BatDongSanService {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    static final String SOURCE_URL = "https://batdongsan.com.vn";
    private static final Integer PAGE_DEEP = 2;
    private ProductManagementRemoteBean productManagementBean;
    private CategoryManagementRemoteBean categoryManagementBean;

    public void setCategoryManagementBean(CategoryManagementRemoteBean categoryManagementBean) {
        this.categoryManagementBean = categoryManagementBean;
    }

    public void setProductManagementBean(ProductManagementRemoteBean productManagementBean) {
        this.productManagementBean = productManagementBean;
    }

    @Override
    public void crawler() throws Exception{
        List<CategoryDTO> categoryDTOs = categoryManagementBean.findAll(true);
        for(CategoryDTO categoryDTO : categoryDTOs){
            for(int i = 0; i < PAGE_DEEP; i++){
                String url = categoryDTO.getUrl() + (i > 1 ? "/p" + i : "");
                logger.info("--- Gathering: " + url);
                List<BatDongSanDTO> items = gatherInfo(url);
                logger.info("--- Processing list: " + items.size());
                try {
                    Integer[] saveInfos = productManagementBean.saveOrUpdate(categoryDTO.getCategoryId(), items);
                    logger.info("--- Result ----");
                    logger.info("--->> [saved: " + saveInfos[0] + ", exists: " + saveInfos[1] + ", error: " + saveInfos[2] + "]");
                }catch (Exception e){
                    logger.info(e.getMessage());
                    continue;
                }
            }
            categoryManagementBean.updateCrawlerStatus(categoryDTO.getCategoryId(), Constants.CRAWLER_DONE);

        }

        for(int i = 1; i < 2; i++){
            String testUrl = "http://batdongsan.com.vn/ban-nha-rieng-tp-hcm" + (i > 1 ? "/p" + i : "");
            //List<BatDongSanDTO> items = gatherInfo(testUrl);
            //productManagementBean.saveOrUpdate(items);
           //listItems.addAll(items);
        }

    }

    @Override
    public void updateMainCategory() {
        List<CategoryTreeDTO> trees = new ArrayList<CategoryTreeDTO>();
        try{
            Document doc = Jsoup.connect(SOURCE_URL).userAgent(Constants.userAgent).get();
            Elements elements = doc.getElementsByClass("dropdown-navigative-menu").select("li.lv0");

            if(elements != null && elements.size() > 0){
                for(Element element : elements){
                    Elements aElm = element.getElementsByTag("a").not("li.lv1 a");
                    if(aElm != null){
                        CategoryTreeDTO tree = new CategoryTreeDTO();
                        CategoryDTO root = new CategoryDTO();
                        String text = aElm.get(0).text();
                        String url = aElm.get(0).attr("href");
                        root.setUrl(SOURCE_URL + url);
                        root.setName(text);
                        tree.setRoot(root);

                        Elements liElm = element.getElementsByTag("ul").select("li.lv1");
                        for(Element li : liElm){
                            Elements a1Elm = li.getElementsByTag("a");
                            if(aElm != null){
                                CategoryDTO children = new CategoryDTO();
                                String ctext = a1Elm.get(0).text();
                                String curl = a1Elm.get(0).attr("href");
                                children.setName(ctext);
                                children.setUrl(SOURCE_URL + curl);
                                if(tree.getNodes() == null){
                                    tree.setNodes(new ArrayList<CategoryDTO>());
                                }
                                tree.getNodes().add(children);
                            }
                        }
                        trees.add(tree);
                    }
                }
            }
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        if(trees != null){
            categoryManagementBean.updateMainCategory(trees);
        }
    }

    private List<BatDongSanDTO> gatherInfo(String url){
        List<BatDongSanDTO> items = new ArrayList<BatDongSanDTO>();
        try{
            Document doc = Jsoup.connect(url).userAgent(Constants.userAgent).get();
            Elements searchProductItems = doc.getElementsByClass("search-productItem");

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
        String pTitle = searchProductItem.select("div.p-title").select("a").attr("title");
        String href = searchProductItem.select("div.p-title").select("a").attr("href");
        String brief = searchProductItem.select("div.p-main").select("div.p-content").select("div.p-main-text").text();
        String pThumb = searchProductItem.select("div.p-main").select("div.p-main-image-crop").select("a").select("img").attr("src");
        String pPrice = searchProductItem.select("div.p-main").select("div.p-bottom-crop").select("div.floatleft").select("span.product-price").text();
        String pArea = searchProductItem.select("div.p-main").select("div.p-bottom-crop").select("div.floatleft").select("span.product-area").text();
        String cityDist = searchProductItem.select("div.p-main").select("div.p-bottom-crop").select("div.floatleft").select("span.product-city-dist").text();

        BatDongSanDTO batDongSanDTO = new BatDongSanDTO();
        batDongSanDTO.setTitle(pTitle);
        batDongSanDTO.setHref(SOURCE_URL + href);
        batDongSanDTO.setThumb(pThumb);
        batDongSanDTO.setPriceString(pPrice);
        batDongSanDTO.setArea(pArea);
        batDongSanDTO.setCityDist(cityDist);
        batDongSanDTO.setBrief(brief);
        batDongSanDTO.setSource(SOURCE_URL);
        try{
            updateDetail(SOURCE_URL + href, batDongSanDTO);
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return batDongSanDTO;
    }

    private void updateDetail(String fullUrl, BatDongSanDTO dto) throws Exception{
        Document doc = Jsoup.connect(fullUrl).userAgent(Constants.userAgent).get();
        String detail = doc.getElementById("product-detail").select("div.pm-desc").html();

        Elements divDacDiemBatDongSan = doc.getElementById("product-detail").select("div.div-table-cell.table1").select("div.div-hold").select("div.row");
        //logger.info("------- DAC DIEM ----------");
        for(Element dacDiem : divDacDiemBatDongSan){
            String key = dacDiem.select("div.left").text().trim();
            String value = dacDiem.select("div.right").text();
            logger.info("[" + key + ": " + value + "]");

            switch (key){
                case "Địa chỉ": dto.setAddress(value);break;
                //case "Mã tin đăng": dto.setCode(value);break;
                case "Loại tin rao": dto.setGroup(value);break;
                case "Ngày đăng": dto.setPostDateStr(value);break;
                case "Ngày hết hạn": dto.setExpireDateStr(value);break;
                case "Số phòng ngủ": dto.setRoom(value);break;
                case "Số toilet": dto.setToilet(value);break;
                case "Hướng nhà": dto.setDirectionString(value);break;
                case "Số tầng": dto.setFloor(value);break;
                case "Mặt tiền": dto.setWidthSize(value);break;
                default:
            }
        }

        //logger.info("------- LIEN HE ----------");
        Elements divLienHe = doc.getElementById("divCustomerInfo").select("div.right-content");
        for(Element lienHe : divLienHe){
            String key = lienHe.select("div.left").text().trim();
            String value = lienHe.select("div.right").text();
            //logger.info("[" + key + ": " + value + "]");
            switch (key){
                case "Tên liên lạc": dto.setContactName(value);break;
                case "Địa chỉ": dto.setContactAddress(value);break;
                case "Điện thoại": dto.setContactPhone(value);break;
                case "Mobile": dto.setContactMobile(value);break;
                case "Email": {
                    if(EmailValidator.validate(value)){
                        dto.setContactEmail(value);
                    }
                    break;
                }
                default:
            }
        }

        // ma tin dang, loai tin dang
        Elements productType =  doc.getElementById("product-detail").select(" > div.prd-more-info").select(" > div");
        for(Element prType : productType){
            String key = prType.select("span.normalblue").text().trim();
            String value = prType.text().split(":")[1].trim();
            //logger.info("[" + key + ": " + value + "]");
            switch (key){
                case "Mã tin đăng:": dto.setCode(value);break;
                case "Ngày đăng:": dto.setPostDateStr(value);break;
                case "Ngày hết hạn:": dto.setExpireDateStr(value);break;
                default:
            }
        }

        Elements imageElements = doc.getElementById("product-detail").select("div.list-img").select("ul").select("li").select("img");
        List<String> images = new ArrayList<String>();
        for(Element imageElement: imageElements){
            images.add(imageElement.attr("src"));
        }

        String latitude = doc.getElementById("hdLat").attr("value");
        String longitude = doc.getElementById("hdLong").attr("value");
        String ward = doc.getElementById("divWardOptions").select("li.current").text();
        String street = doc.getElementById("divStreetOptions").select("li.current").text();
        String brand = doc.getElementById("divProjectOptions").select("li.current").text();
        String room = doc.getElementById("divBedRoomOptions").select("li.current").text();

        dto.setRoom(room);
        dto.setWardString(ward);
        dto.setStreetString(street);
        dto.setBrandString(brand);
        dto.setLongitude(longitude);
        dto.setLatitude(latitude);
        dto.setImages(images);
        dto.setDetail(detail);
    }
}
