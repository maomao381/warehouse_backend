package com.wanglei.service.impl;

import com.wanglei.mapper.GoodsMapper;
import com.wanglei.pojo.Goods;
import com.wanglei.pojo.GoodsSum;
import com.wanglei.pojo.Inventory;
import com.wanglei.pojo.ShelveInfo;
import com.wanglei.service.GoodsService;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<Goods> getGoodsList(Integer type, String[] keywords, Integer page){
        switch (type) {
            case 0:
                return goodsMapper.getGoodsAll(page);
            case 1: // 按全部查找
                return goodsMapper.getGoodsByKeywords(keywords, page);
            case 2: // 按材料查找
                return goodsMapper.getGoodsByMaterial(keywords, page);
            case 3: // 按产地查找
                return goodsMapper.getGoodsByArea(keywords, page);
                default: return null;
        }
    }

    @Override
    public List<Goods> getAccountList(Integer goodsId, String[] keywords, Integer page) {
        return goodsMapper.getAccountAll(goodsId, keywords, page);
    }

    @Override
    public List<Goods> getStoreList(Integer goodsId, Integer page) {
        return goodsMapper.getStoreAll(goodsId, page);
    }

    @Override
    public List<Goods> getDeliverList(Integer goodsId, Integer page) {
        return goodsMapper.getDeliverAll(goodsId, page);
    }

    @Override
    public boolean updateGoods(Integer materialId, Integer areaId, Integer goodsId, String updateDate, String deliverOwner, Float updateSize, Integer behavior, String username, Integer place) {
        try {
            // 没传goodsid
            if (goodsId == 0) {
                if (goodsMapper.isExist(materialId, areaId) == 0) {
                    if (behavior == 0) {
                        goodsMapper.insertGoods(materialId, areaId, updateSize, updateDate);
                        goodsId = goodsMapper.getId(materialId, areaId);
                        goodsMapper.insertAccount(goodsId, updateDate, deliverOwner, updateSize, behavior, username, place);
                    }
                    else {
                        return false;
                    }
                }
                else {
                        if (behavior == 0) {
                            goodsId = goodsMapper.getId(materialId, areaId);
                            goodsMapper.insertAccount(goodsId, updateDate, deliverOwner, updateSize, behavior, username, place);
                            goodsMapper.updateAccount(behavior, updateSize, updateDate, goodsId);
                        }
                        else {
                            goodsMapper.insertAccount(goodsId, updateDate, deliverOwner, updateSize, behavior, username, place);
                            goodsMapper.updateAccount(behavior, updateSize, updateDate, goodsId);
                        }
                }

            }
            // 传了goodsid
            else {
                        goodsMapper.insertAccount(goodsId, updateDate, deliverOwner, updateSize, behavior, username, place);
                        goodsMapper.updateAccount(behavior, updateSize, updateDate, goodsId);
            }
            return true;
        }
        catch (Error e) {
            return false;
        }
    }

    @Override
    public boolean updateGoodsShelve(Integer goodsId, Float updateSize, Integer behavior, Integer place) {
        try {
            if (behavior == 0) {
                goodsMapper.placeAccount(goodsId, place);
            }
                if (goodsMapper.isExistShelve(goodsId, place) == 0) {
                    if (behavior == 0) {
                        goodsMapper.insertShelve(goodsId, place, updateSize);
                    }
                    else {
                        return false;
                    }
                }
                else {
                    Integer shelveId = goodsMapper.getIdShelve(goodsId, place);
                    goodsMapper.updateShelve(behavior, updateSize, shelveId);
                }
            return true;
        }
        catch (Error e) {
            return false;
        }
    }

    @Override
    public List<Goods> getAccountByDate(Integer type, Integer goodsId, Integer condition, Integer page) {
        Integer year = condition / 10;
        Integer season = condition - year * 10;
        switch (type) {
            case 0: return goodsMapper.getAccountToday(goodsId, page);
            case 1: return goodsMapper.getAccountYesterday(goodsId, page);
            case 2: return goodsMapper.getAccountThisWeek(goodsId, page);
            case 3: return goodsMapper.getAccountByMonth(goodsId, condition, page);
            case 4: return goodsMapper.getAccountBySeason(goodsId, year, season, page);
            case 5: return goodsMapper.getAccountByYear(goodsId, condition, page);
            default: return null;
        }
    }

    @Override
    public List<GoodsSum> getGoodsGraph(Integer type, Integer goodsId, Integer condition) {
        switch (type) {
            case 0: return goodsMapper.getGraphByYear(goodsId, condition);
            case 1: return goodsMapper.getGraphByDate(goodsId, condition);
            default:return null;
        }
    }

    @Override
    public Integer getGoodsCount(Integer type, String[] keywords) {
        switch (type) {
            case 0:
                return goodsMapper.getGoodsCount();
            case 1: // 按全部查找
                return goodsMapper.getGoodsCountByKeywords(keywords);
            case 2: // 按材料查找
                return goodsMapper.getGoodsCountByMaterial(keywords);
            case 3: // 按产地查找
                return goodsMapper.getGoodsCountByArea(keywords);
            default: return 0;
        }
    }

    @Override
    public Integer getAccountCount(Integer goodsId, String[] keywords) {
        return goodsMapper.getAccountCount(goodsId, keywords);
    }

    @Override
    public Integer getStoreCount(Integer goodsId) {
        return goodsMapper.getStoreCount(goodsId);
    }

    @Override
    public Integer getDeliverCount(Integer goodsId) {
        return goodsMapper.getDeliverCount(goodsId);
    }

    @Override
    public Integer getAccountCountByDate(Integer type, Integer goodsId, Integer condition) {
        Integer year = condition / 10;
        Integer season = condition - year * 10;
        switch (type) {
            case 0:
                return goodsMapper.getAccountCountToday(goodsId);
            case 1:
                return goodsMapper.getAccountCountYesterday(goodsId);
            case 2:
                return goodsMapper.getAccountCountThisWeek(goodsId);
            case 3:
                return goodsMapper.getAccountCountByMonth(goodsId, condition);
            case 4:
                return goodsMapper.getAccountCountBySeason(goodsId, year, season);
            case 5:
                return goodsMapper.getAccountCountByYear(goodsId, condition);
            default:
                return null;
        }
    }

    @Override
    public List<GoodsSum> getGoodsSumByArea(Integer type) {
        switch (type) {
            case 0:
                return goodsMapper.getCurrentSumByArea();
            case 1:
                return goodsMapper.getStoreSumByArea();
            case 2:
                return goodsMapper.getDeliverSumByArea();
                default: return null;
        }
    }

    @Override
    public List<GoodsSum> getGoodsSumByMaterial(Integer type) {
        switch (type) {
            case 0:
                return goodsMapper.getCurrentSumByMaterial();
            case 1:
                return goodsMapper.getStoreSumByMaterial();
            case 2:
                return goodsMapper.getDeliverSumByMaterial();
            default: return null;
        }
    }

    @Override
    public List<GoodsSum> getGoodsSellSum(Integer type, Integer id) {
        switch (type) {
            case 0: return goodsMapper.getSellSumByMaterial(id);
            case 1: return goodsMapper.getSellSumByArea(id);
            default: return null;
        }
    }

    @Override
    public boolean alterGoodsSize(Integer goodsId, String updateDate) {
        // 0是变多 1是变少
        goodsMapper.alterGoods(goodsId, updateDate);
        return true;
    }

    @Override
    public boolean deleteGoods(Integer goodsId) {
        goodsMapper.deleteGoods(goodsId);
        return true;
    }

    @Override
    public List<ShelveInfo> getShelveInfo(Integer floor) {
        return goodsMapper.getShelveInfo(floor);
    }

    @Override
    public List<ShelveInfo> getShelveRecommend() {
        return goodsMapper.getShelveRecommend();
    }

    @Override
    public boolean initShelveInfo(Integer rowNum, Integer colNum, Integer floorNum, Integer capacity) {
        if (rowNum > 26) return false;
        Integer rowNo;
        Integer colNo;
        Integer floorNo;
        for (rowNo = 1;rowNo <= rowNum; rowNo++) {
            for (colNo = 1; colNo <= colNum; colNo++) {
                for (floorNo = 1; floorNo <= floorNum; floorNo++) {
                    char rowName = (char) ((rowNo - 1) % 26 + (int) 'A');
                    String colName = String.valueOf(colNo);
                    String floorName = String.valueOf(floorNo);
                    String name = rowName + "-" + colName + "-" + floorName;
                    goodsMapper.insertShelveInfo(rowNo, colNo, floorNo, capacity, name);
                }
            }
        }
        return true;
    }

    @Override
    public boolean insertShelveInfo(Integer rowNo, Integer colNum, Integer floorNum, Integer capacity) {
        if (rowNo > 26) return false;
        Integer colNo;
        Integer floorNo;
        for (colNo = 1; colNo <= colNum; colNo++) {
            for (floorNo = 1; floorNo <= floorNum; floorNo++) {
                char rowName = (char) ((rowNo - 1) % 26 + (int) 'A');
                char colName = (char) (colNo % 27);
                char floorName = (char) (floorNo % 27);
                String name = rowName + "-" + colName + "-" + floorName;
                goodsMapper.insertShelveInfo(rowNo, colNo, floorNo, capacity, name);
            }
        }
        return true;
    }

    @Override
    public List<ShelveInfo> queryGoodsShelve(Integer goodsId) {
        return goodsMapper.queryGoodsShelve(goodsId);
    }

    @Override
    public List<ShelveInfo> queryShelveGoods(Integer place) {
        return goodsMapper.queryShelveGoods(place);
    }
}
