package indi.a9043.gree_scanning.service;

import indi.a9043.gree_scanning.mapper.CommMapper;
import indi.a9043.gree_scanning.pojo.Comm;
import indi.a9043.gree_scanning.pojo.CommExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.*;

/**
 * @author a9043 卢学能 zzz13129180808@gmail.com
 */
@Service("dataService")
public class DataService {
    @Resource
    private CommMapper commMapper;

    public List<Comm> selectComm(String voucher, String barcode, Date startDate, Date endDate, long start, long end) {
        CommExample commExample = new CommExample();
        CommExample.Criteria criteria = commExample.createCriteria();

        if (voucher != null && !voucher.equals("")) {
            criteria.andVoucherLike("%" + voucher + "%");
        }

        if (barcode != null && !barcode.equals("")) {
            criteria.andBarcodeEqualTo(barcode);
        }

        if (startDate != null) {
            criteria.andDateTimeGreaterThanOrEqualTo(startDate);
        }

        if (endDate != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(endDate);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            endDate.setTime(calendar.getTime().getTime());
            criteria.andDateTimeLessThan(endDate);
        }

        commExample.setStart(start);
        commExample.setEnd(end);

        return commMapper.selectByExample(commExample);
    }

    @Transactional
    public Integer addNewData(Comm comm) {
        return commMapper.insert(comm);
    }

    @Transactional
    public int[] addNewData(List<Comm> commList) {
        int count = 0;
        for (Comm comm : commList) {
            count += commMapper.insert(comm);
        }

        if (commList.size() > 0) {
            return new int[]{0, count};
        } else {
            return new int[]{0, 0};
        }
    }

    @Transactional
    public List<String> deleteComm(List<Map<String, String>> mapList) {
        List<String> stringList = new ArrayList<String>();
        CommExample commExample = new CommExample();
        for (Map<String, String> map : mapList) {
            commExample.createCriteria().andVoucherEqualTo(map.get("voucher")).andBarcodeEqualTo(map.get("barcode"));
            if (commMapper.deleteByExample(commExample) <= 0) {
                stringList.add(map.get("voucher") + ", " + map.get("barcode"));
            }
            commExample.clear();
        }
        return stringList;
    }

    public long getPageCount(int pageSize, String voucher, String barcode, Date startDate, Date endDate) {
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
        long count = commMapper.countByExample(commExample);
        return (count - 1) / pageSize + 1;
    }
}
