package indi.a9043.gree_scanning.service;

import indi.a9043.gree_scanning.mapper.CommMapper;
import indi.a9043.gree_scanning.pojo.Comm;
import indi.a9043.gree_scanning.pojo.CommExample;
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
    private CommMapper commMapper;

    public List<Comm> selectComm(String voucher, String barcode, Date startDate, Date endDate) {
        CommExample commExample = new CommExample();
        CommExample.Criteria criteria = commExample.createCriteria();

        if (voucher != null && !voucher.equals("")) {
            criteria.andVoucherEqualTo(voucher);
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

/*        List<String> voucherList = commMapper.selectVoucher(commExample);

        if (voucherList.size() == 0) {
            return new ArrayList<Comm>();
        }*/
        return commMapper.selectByExample(commExample);
    }

    public Integer addNewData(Comm comm) {
        return commMapper.insert(comm);
    }

    public int[] addNewData(List<Comm> commList) {
        List<Map> tempMapList = commMapper.selectCommList(commList);
        List<String> voucherList = new ArrayList<String>();

        for (Map map : tempMapList) {
            if (map.get("VOUCHER") != null) {
                voucherList.add(map.get("VOUCHER").toString());
                continue;
            }
            if (map.get("voucher") != null) {
                voucherList.add(map.get("voucher").toString());
            }
        }

        Iterator<Comm> it = commList.iterator();
        while (it.hasNext()) {
            Comm x = it.next();
            if (voucherList.contains(x.getVoucher())) {
                it.remove();
            }
        }

        if (commList.size() > 0) {
            return new int[]{voucherList.size(), commMapper.insertCommList(commList)};
        } else {
            return new int[]{voucherList.size(), 0};
        }
    }

    public List<String> deleteComm(List<String> voucherList) {
        List<String> stringList = new ArrayList<String>();
        CommExample commExample = new CommExample();
        for (String voucher : voucherList) {
            commExample.createCriteria().andVoucherEqualTo(voucher);
            if (commMapper.deleteByExample(commExample) <= 0) {
                stringList.add(voucher);
            }
            commExample.clear();
        }
        return stringList;
    }
}
