package indi.a9043.gree_scanning.swing.pojo;

public class SearchData {
    private String startDate;
    private String endDate;
    private String voucher;
    private String barcode;

    public SearchData() {
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(final String voucher) {
        this.voucher = voucher;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(final String barcode) {
        this.barcode = barcode;
    }
}