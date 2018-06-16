package indi.a9043.gree_scanning.service;

import indi.a9043.gree_scanning.mapper.GreeScanningMapper;
import indi.a9043.gree_scanning.pojo.GreeScanning;
import indi.a9043.gree_scanning.pojo.GreeScanningExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author a9043 卢学能 zzz13129180808@gmail.com
 */
@Service("dataService")
public class DataService {
    @Resource
    private GreeScanningMapper greeScanningMapper;

    public List<GreeScanning> selectGreeScanning(String voucher, String barcode, Date startDate, Date endDate) {
        GreeScanningExample greeScanningExample = new GreeScanningExample();
        GreeScanningExample.Criteria criteria = greeScanningExample.createCriteria();

        if (voucher != null && !voucher.equals("")) {
            criteria.andVoucherEqualTo(Integer.parseInt(voucher));
        }

        if (barcode != null && !barcode.equals("")) {
            criteria.andBarcodeEqualTo(barcode);
        }

        if (startDate != null) {
            criteria.andDateTimeGreaterThanOrEqualTo(startDate);
        }

        if (endDate != null) {
            criteria.andDateTimeLessThanOrEqualTo(endDate);
        }

        return greeScanningMapper.selectByExample(greeScanningExample);
    }

    public Integer addNewData(GreeScanning greeScanning) {
        return greeScanningMapper.insert(greeScanning);
    }

    public int[] addNewData(List<GreeScanning> greeScanningList) {
        List<Map> tempMapList = greeScanningMapper.selectGreeScanningList(greeScanningList);
        List<Integer> voucherList = new ArrayList<Integer>();

        for (Map map : tempMapList) {
            if (map.get("VOUCHER") != null) {
                voucherList.add(Integer.valueOf(map.get("VOUCHER").toString()));
                continue;
            }
            if (map.get("voucher") != null) {
                voucherList.add(Integer.valueOf(map.get("voucher").toString()));
            }
        }

        Iterator<GreeScanning> it = greeScanningList.iterator();
        while (it.hasNext()) {
            GreeScanning x = it.next();
            if (voucherList.contains(x.getVoucher())) {
                it.remove();
            }
        }

        if (greeScanningList.size() > 0) {
            return new int[]{voucherList.size(), greeScanningMapper.insertGreeScanningList(greeScanningList)};
        } else {
            return new int[]{voucherList.size(), 0};
        }
    }

    public List<String> deleteGreeScanning(List<String> voucherList) {
        List<String> stringList = new ArrayList<String>();
        for (String voucher : voucherList) {
            if (greeScanningMapper.deleteByPrimaryKey(Integer.valueOf(voucher)) <= 0) {
                stringList.add(voucher);
            }
        }
        return stringList;
    }
}
