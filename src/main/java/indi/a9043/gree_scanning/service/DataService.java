package indi.a9043.gree_scanning.service;

import indi.a9043.gree_scanning.mapper.GreeScanningMapper;
import indi.a9043.gree_scanning.pojo.GreeScanning;
import indi.a9043.gree_scanning.pojo.GreeScanningExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("dataService")
public class DataService {
    @Resource
    private GreeScanningMapper greeScanningMapper;

    public List<GreeScanning> selectGreeScanning(String voucher, String barcode, LocalDate startDate, LocalDate endDate) {
        GreeScanningExample greeScanningExample = new GreeScanningExample();
        GreeScanningExample.Criteria criteria = greeScanningExample.createCriteria();

        Optional.ofNullable(voucher)
                .filter(str -> !str.equals(""))
                .map(Integer::parseInt)
                .map(criteria::andVoucherEqualTo);

        Optional.ofNullable(barcode)
                .filter(str -> !str.equals(""))
                .map(criteria::andBarcodeEqualTo);

        Optional.ofNullable(startDate)
                .map(Date::valueOf)
                .map(criteria::andDateTimeGreaterThanOrEqualTo);

        Optional.ofNullable(endDate)
                .map(Date::valueOf)
                .map(criteria::andDateTimeGreaterThanOrEqualTo);

        return greeScanningMapper.selectByExample(greeScanningExample);
    }

    public Integer addNewData(GreeScanning greeScanning) {
        return greeScanningMapper.insert(greeScanning);
    }

    public List<String> deleteGreeScanning(List<String> voucherList) {
        return voucherList
                .stream()
                .filter(voucher -> greeScanningMapper.deleteByPrimaryKey(Integer.valueOf(voucher)) <= 0)
                .collect(Collectors.toList());
    }
}
