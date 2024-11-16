package com.test.business.impl;

import com.test.business.ProductManagementLocalBean;
import com.test.business.ProductManagementRemoteBean;
import com.test.domain.*;
import com.test.dto.BatDongSanDTO;
import com.test.dto.ProductDTO;
import com.test.session.*;
import com.test.utils.DozerSingletonMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import javax.ejb.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 11:40 AM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "ProductManagementSessionEJB")
public class ProductManagementSessionBean implements ProductManagementRemoteBean, ProductManagementLocalBean {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    @EJB
    private ProductLocalBean productLocalBean;
    @EJB
    private CategoryLocalBean categoryLocalBean;
    @EJB
    private CityLocalBean cityLocalBean;
    @EJB
    private DistrictLocalBean districtLocalBean;
    @EJB
    private DirectionLocalBean directionLocalBean;
    @EJB
    private WardLocalBean wardLocalBean;
    @EJB
    private StreetLocalBean streetLocalBean;
    @EJB
    private BrandLocalBean brandLocalBean;
    @EJB
    private ProductAssetLocalBean productAssetLocalBean;
    @EJB
    private CrawlerHistoryLocalBean crawlerHistoryLocalBean;
    @EJB
    private ProductDetailLocalBean productDetailLocalBean;
    @EJB
    private UnitLocalBean unitLocalBean;

    @Override
    public Integer[] saveOrUpdate(Integer categoryId, List<BatDongSanDTO> items) {
        int saved = 0, exists = 0, error = 0;
        for(BatDongSanDTO batDongSanDTO : items){
            batDongSanDTO = validateData(batDongSanDTO);
            Boolean isExists = productLocalBean.findByCode(batDongSanDTO.getCode());
            if(!isExists){
                Boolean ok = validateRequired(batDongSanDTO);
                if(ok){
                    try{
                        ProductEntity productEntity = saveProduct(categoryId, batDongSanDTO);
                        saveProductAsset(productEntity, batDongSanDTO.getImages());
                        saved++;
                    }catch (DuplicateKeyException e){
                        error++;
                    }
                }else{
                    logger.log(Level.ALL, "Validate fail");
                    error++;
                }
            }else{
                logger.log(Level.ALL, "Product exists");
                exists++;
            }
        }
        Integer[] result = new Integer[3];
        result[0] = saved;
        result[1] = exists;
        result[2] = error;
        return result;
    }

    @Override
    public void updateCrawlerHistory(String siteUrl, Integer saved, Integer exists, Integer error){
        try{
            CrawlerHistoryEntity crawlerHistoryEntity = new CrawlerHistoryEntity();
            crawlerHistoryEntity = new CrawlerHistoryEntity();
            crawlerHistoryEntity.setAdded(saved);
            crawlerHistoryEntity.setSkip(exists);
            crawlerHistoryEntity.setError(error);
            crawlerHistoryEntity.setSiteUrl(siteUrl);
            crawlerHistoryEntity.setCrawlerDate(new Timestamp(System.currentTimeMillis()));
            crawlerHistoryLocalBean.save(crawlerHistoryEntity);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Boolean validateRequired(BatDongSanDTO batDongSanDTO){
        Boolean ok = Boolean.TRUE;
        if(StringUtils.isBlank(batDongSanDTO.getCode())){
            ok = Boolean.FALSE;
        }else if(StringUtils.isBlank(batDongSanDTO.getTitle())){
            ok = Boolean.FALSE;
        }else if(StringUtils.isBlank(batDongSanDTO.getBrief())){
            ok = Boolean.FALSE;
        }else if(StringUtils.isBlank(batDongSanDTO.getPostDateStr())){
            ok = Boolean.FALSE;
        }

        return ok;
    }

    private void saveProductAsset(ProductEntity productEntity, List<String> images) {
        if(images != null && images.size() > 0){
            for(String img : images){
                ProductAssetEntity productAssetEntity = new ProductAssetEntity();
                productAssetEntity.setOrgUrl(img);
                productAssetEntity.setUrl(img);
                productAssetEntity.setProduct(productEntity);
                try{
                    productAssetLocalBean.save(productAssetEntity);
                }catch (DuplicateKeyException e){}
            }
        }
    }

    private ProductEntity saveProduct(Integer categoryId, BatDongSanDTO batDongSanDTO) throws DuplicateKeyException{
        ProductEntity productEntity = DozerSingletonMapper.getInstance().map(batDongSanDTO, ProductEntity.class);
        ProductDetailEntity  productDetailEntity = DozerSingletonMapper.getInstance().map(batDongSanDTO, ProductDetailEntity.class);


        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(categoryId);
        productEntity.setCategory(categoryEntity);

        if(StringUtils.isNotBlank(batDongSanDTO.getCityDist())){
            String[] strs = batDongSanDTO.getCityDist().split(",");
            if(strs.length == 2){
                String city = strs[1].replace("TP.", "").replace("Thành phố", "").replace("thành phố", "").replace("Thành Phố", "").replace("Tp.", "").replace("tp.", "").trim();
                String district = strs[0].replace("Quận", "").replace("Huyện", "").replace("Thị xã", "").replace("Thị Xã", "").replace("Tp.", "").replace("TP.", "").replace("tp.", "").replace("Thành phố", "").replace("thành phố", "").replace("Thành Phố", "").trim();
                if(StringUtils.isNumeric(district)){
                    district = strs[0].replace("Huyện", "").replace("Thị xã", "").replace("Thị Xã", "").trim();
                }
                CityEntity cityEntity = saveOrLoadCity(city);
                if(cityEntity != null){
                    productEntity.setCity(cityEntity);
                    DistrictEntity districtEntity = saveOrLoadDistrict(cityEntity, district);
                    if(districtEntity != null){
                        productEntity.setDistrict(districtEntity);
                        WardEntity wardEntity = saveOrLoadWard(districtEntity, batDongSanDTO.getWardString());
                        if(wardEntity != null){
                            productEntity.setWard(wardEntity);
                            productEntity.setStreet(batDongSanDTO.getStreetString());
                        }
                    }
                }
            }
        }


        BrandEntity brandEntity = saveOrLoadBrand(batDongSanDTO.getBrandString(), batDongSanDTO.getSource());
        if(brandEntity != null){
            productEntity.setBrand(brandEntity);
        }

        UnitEntity unitEntity = saveOrLoadUnit(batDongSanDTO.getUnitString());
        if(unitEntity != null){
            productEntity.setUnit(unitEntity);
        }

        Timestamp postDate = parseDate(batDongSanDTO.getPostDateStr());
        productEntity.setPostDate(postDate);
        productEntity.setModifiedDate(postDate);
        if(StringUtils.isNotBlank(batDongSanDTO.getExpireDateStr())) {
            productEntity.setExpireDate(parseDate(batDongSanDTO.getExpireDateStr()));
        }else{
            Calendar cal = Calendar.getInstance();
            cal.setTime(productEntity.getPostDate());
            cal.add(Calendar.MONTH, 1);
            productEntity.setExpireDate(new Timestamp(cal.getTime().getTime()));
        }
        productEntity.setView(0);

        productEntity = productLocalBean.save(productEntity);

        // Save Product Detail
        productDetailEntity.setProduct(productEntity);
        DirectionEntity directionEntity = saveOrLoadDirection(batDongSanDTO.getDirectionString());
        if(directionEntity != null){
            productDetailEntity.setDirection(directionEntity);
        }
        productDetailLocalBean.save(productDetailEntity);

        return productEntity;
    }

    private UnitEntity saveOrLoadUnit(String unitString){
        UnitEntity unitEntity = null;
        if(StringUtils.isNotBlank(unitString)){
            try{
                unitEntity = unitLocalBean.findEqualUnique("title", unitString);
            }catch (ObjectNotFoundException e){
                unitEntity = new UnitEntity();
                unitEntity.setTitle(unitString);
                try{
                    unitEntity = unitLocalBean.save(unitEntity);
                }catch (DuplicateKeyException de){}
            }
        }
        return unitEntity;
    }

    private BrandEntity saveOrLoadBrand(String brandString, String source){
        BrandEntity brandEntity = null;
        if(StringUtils.isNotBlank(brandString)){
            try{
                brandEntity = brandLocalBean.findEqualUnique("brandName", brandString);
            }catch (ObjectNotFoundException e){
                brandEntity = new BrandEntity();
                brandEntity.setBrandName(brandString);
                brandEntity.setDescription(brandString);
                try{
                    brandEntity = brandLocalBean.save(brandEntity);
                }catch (DuplicateKeyException de){}
            }
        }
        return brandEntity;
    }

    private StreetEntity saveOrLoadStreet(WardEntity wardEntity, String street){
        StreetEntity streetEntity = null;
        try{
            streetEntity = streetLocalBean.findEqualUnique("streetName", street);
        }catch (ObjectNotFoundException e){
            streetEntity = new StreetEntity();
            streetEntity.setWard(wardEntity);
            streetEntity.setStreetName(street);
            try{
                streetEntity = streetLocalBean.save(streetEntity);
            }catch (DuplicateKeyException du){}
        }
        return streetEntity;
    }

    private WardEntity saveOrLoadWard(DistrictEntity districtEntity, String ward){
        WardEntity wardEntity = null;
        if(StringUtils.isNotBlank(ward)){
            try{
                ward = ward.replace("Phường", "").replace("phường", "").replace("Xã", "").replace("xã", "").trim();
                wardEntity = wardLocalBean.findEqualUnique("wardName", ward);
            }catch (ObjectNotFoundException e){
                wardEntity = new WardEntity();
                wardEntity.setDistrict(districtEntity);
                wardEntity.setWardName(ward);
                try{
                    wardEntity = wardLocalBean.save(wardEntity);
                }catch (DuplicateKeyException de){}
            }
        }
        return wardEntity;
    }

    private Timestamp parseDate(String dateStr){
        Timestamp result = null;
        if(StringUtils.isNotBlank(dateStr)){
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            try{
                Date date = format.parse(dateStr);
                result = new Timestamp(date.getTime());
            }catch (ParseException e){}
        }
        return result;
    }

    private DirectionEntity saveOrLoadDirection(String direction){
        DirectionEntity directionEntity = null;
        if(StringUtils.isNotBlank(direction)){
            try{
                directionEntity = directionLocalBean.findEqualUnique("directionName", direction);
            }catch (ObjectNotFoundException e){
                directionEntity = new DirectionEntity();
                directionEntity.setDirectionName(direction);
                try{
                    directionEntity = directionLocalBean.save(directionEntity);
                }catch (DuplicateKeyException de){}
            }
        }
        return directionEntity;
    }

    private DistrictEntity saveOrLoadDistrict(CityEntity city, String district) {
        DistrictEntity districtEntity = null;
        districtEntity = districtLocalBean.findByNameAndCityId(district, city.getCityId());
        if(districtEntity == null){
            districtEntity = new DistrictEntity();
            districtEntity.setDistrictName(district);
            districtEntity.setCity(city);
            try{
                districtEntity = districtLocalBean.save(districtEntity);
            }catch (DuplicateKeyException de){}
        }
        return districtEntity;
    }

    private CityEntity saveOrLoadCity(String city){
        CityEntity cityEntity = null;
        city = city.replace("-", "").replace("  ", " ");
        cityEntity = cityLocalBean.findByName(city);
        if(cityEntity == null){
            if(city.equals("Hà Nội") || city.equalsIgnoreCase("Hà Nội") || (city.length() == 6 && !city.toLowerCase().contains("nam")) ){
                try {
                    cityEntity = cityLocalBean.findEqualUnique("cityName", "Hà Nội");
                }catch (ObjectNotFoundException e){}
            }else if(city.equals("Hà Nội")){

            }
            if(cityEntity == null){
                cityEntity = new CityEntity();
                cityEntity.setCityName(city);
                try{
                    cityEntity = cityLocalBean.save(cityEntity);
                }catch (DuplicateKeyException de){}
            }

        }
        return cityEntity;
    }

    @Override
    public Object[] searchByProperties(Map<String, Object> properties, String sortExpression, String sortDirection, int firstItem, int maxPageItems) {
        Object[] objs = productLocalBean.searchByProperties(properties, sortExpression, sortDirection, firstItem, maxPageItems);
        List<ProductDTO> productDTOs = new ArrayList<>();
        List<ProductEntity> productEntities = (List<ProductEntity>)objs[1];
        for(ProductEntity productEntity : productEntities){
            productDTOs.add(DozerSingletonMapper.getInstance().map(productEntity, ProductDTO.class));
        }
        Object[] result = new Object[2];
        result[0] = objs[0];
        result[1] = productDTOs;
        return result;
    }

    @Override
    public void mergeProduct(long city1, long city2) {
        List<DistrictEntity> allDistricts = districtLocalBean.findAll();
        List<Integer> deleteWardIds = new ArrayList<>();
        for(DistrictEntity districtEntity : allDistricts){
            List<WardEntity> wards = wardLocalBean.findByDistrictId(districtEntity.getDistrictId());
            for(WardEntity wardEntity : wards){
                if(wardEntity.getWardName().toLowerCase().startsWith("phường")){
                    String searchName = wardEntity.getWardName().replace("Phường", "").replace("phường", "").trim();
                    try{
                        List<WardEntity> findWards = (List<WardEntity>) wardLocalBean.findProperty("wardName", searchName);
                        if(findWards.size() > 0) {
                            //if (findWards.get(0).getDistrict().getDistrictId().equals(wardEntity.getDistrict().getDistrictId())) {
                                productLocalBean.updateNewWard4Product(wardEntity.getWardId(), findWards.get(0).getWardId(), wardEntity.getDistrict().getDistrictId());
                                deleteWardIds.add(wardEntity.getWardId());
                            //}
                        }
                    }catch (Exception e){
                        continue;
                    }
                }
            }
        }

        if(deleteWardIds.size() > 0){
            wardLocalBean.deleteByListIds(deleteWardIds);
        }


    }


    private BatDongSanDTO validateData(BatDongSanDTO dto){
        BatDongSanDTO result = dto;
        return result;
    }


}
