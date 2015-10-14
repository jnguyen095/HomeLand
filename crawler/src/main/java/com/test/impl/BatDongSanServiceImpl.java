package com.test.impl;

import com.test.BatDongSanService;
import com.test.Constants;
import com.test.business.CategoryManagementRemoteBean;
import com.test.dto.BatDongSanDTO;
import com.test.dto.CategoryDTO;
import com.test.dto.CategoryTreeDTO;
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
    static final String SOURCE_URL = "http://batdongsan.com.vn";

    public void setCategoryManagementBean(CategoryManagementRemoteBean categoryManagementBean) {
        this.categoryManagementBean = categoryManagementBean;
    }

    private CategoryManagementRemoteBean categoryManagementBean;

    @Override
    public void crawler() throws Exception{
        String testUrl = "http://batdongsan.com.vn/ban-nha-rieng-tp-hcm";
        //String testUrl = "http://batdongsan.com.vn/ban-nha-rieng-ha-noi";
        List<BatDongSanDTO> listItems = new ArrayList<BatDongSanDTO>();
        for(int i = 1; i < 2; i++){
            testUrl = "http://batdongsan.com.vn/ban-nha-rieng-tp-hcm" + (i > 1 ? "/p" + i : "");
            List<BatDongSanDTO> items = getBrief(testUrl);
            listItems.addAll(items);
        }

    }

    @Override
    public void updateMainCategory() {
        String fullUrl = "http://batdongsan.com.vn";
        List<CategoryTreeDTO> trees = new ArrayList<CategoryTreeDTO>();
        try{
            Document doc = Jsoup.connect(fullUrl).userAgent(Constants.userAgent).get();
            Elements elements = doc.getElementsByClass("dropdown-navigative-menu").select("li.lv0");

            if(elements != null && elements.size() > 0){
                for(Element element : elements){
                    Elements aElm = element.getElementsByTag("a").not("li.lv1 a");
                    if(aElm != null){
                        CategoryTreeDTO tree = new CategoryTreeDTO();
                        CategoryDTO root = new CategoryDTO();
                        String text = aElm.get(0).text();
                        String url = aElm.get(0).attr("href");
                        root.setUrl(fullUrl + url);
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
                                children.setUrl(fullUrl + curl);
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

    private void updateDetail(String fullUrl, BatDongSanDTO dto) throws Exception{
        Document doc = Jsoup.connect(fullUrl).userAgent(Constants.userAgent).get();
        String detail = doc.getElementById("product-detail").select("div.pm-content").html();

        Elements divDacDiemBatDongSan = doc.getElementsByClass("pm-content-detail").select("div.left-detail").select(" > div");
        //logger.info("------- DAC DIEM ----------");
        for(Element dacDiem : divDacDiemBatDongSan){
            String key = dacDiem.select("div.left").text().trim();
            String value = dacDiem.select("div.right").text();
            //logger.info("[" + key + ": " + value + "]");

            switch (key){
                case "Địa chỉ": dto.setAddress(value);break;
                case "Mã số": dto.setCode(value);break;
                case "Loại tin rao": dto.setType(value);break;
                case "Ngày đăng tin": dto.setPostDate(value);break;
                case "Ngày hết hạn": dto.setExpireDate(value);break;
                case "Số phòng ngủ": dto.setRoom(value);break;
                case "Số toilet": dto.setToilet(value);break;
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
                case "Email": dto.setContactEmail(value);break;
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

        dto.setLongitude(longitude);
        dto.setLatitude(latitude);
        dto.setImages(images);
        dto.setDetail(detail);
    }

    private List<BatDongSanDTO> getBrief(String url){
        List<BatDongSanDTO> items = new ArrayList<BatDongSanDTO>();
        try{
            Document doc = Jsoup.connect(url).userAgent(Constants.userAgent).get();
            Elements searchProductItems = doc.getElementsByClass("search-productItem");

            for (Element searchProductItem : searchProductItems) {
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
                batDongSanDTO.setPrice(pPrice);
                batDongSanDTO.setArea(pArea);
                batDongSanDTO.setCityDist(cityDist);
                batDongSanDTO.setBrief(brief);
                try{
                    updateDetail(SOURCE_URL + href, batDongSanDTO);
                }catch (Exception e){
                    continue;
                }

                items.add(batDongSanDTO);
            }
        }catch (IOException e){
            System.out.print("Exception: " + e.getMessage());
        }
        return items;
    }
}
