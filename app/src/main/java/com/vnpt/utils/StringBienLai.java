package com.vnpt.utils;

public class StringBienLai {

    private static final String[] MANG_SO = { "không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín" };

    public static String getStringBienLai(String key, String cuscode, String cusname, String cusaddress, String cusphone, String custaxcode, String paymentmethod, String prodName, String total, String amount, String amountinwords, String email, String userName)
    {
        if (key == null)
            key = genKey(userName);
        String xml = "<Invoices>";
        xml += "<Inv>";
        xml += "<key>" + key + "</key>";
        xml += "<Invoice>";
        if (cuscode == null || "".equals(cuscode.trim()))
            cuscode = System.currentTimeMillis() + "";
        xml += "<CusCode><![CDATA[" + cuscode + "]]></CusCode>";
        xml += "<CusName><![CDATA[" + cusname + "]]></CusName>";
        xml += "<CusAddress><![CDATA[" + cusaddress + "]]></CusAddress>";
        if (email != null && email.trim() != "")
        {
            xml += "<Email><![CDATA[" + email + "]]></Email>";
        }
        xml += "<CusPhone>" + cusphone + "</CusPhone>";
        xml += "<CusTaxCode>" + custaxcode + "</CusTaxCode>";
        xml += "<PaymentMethod><![CDATA[" + paymentmethod + "]]></PaymentMethod>";
        xml += "<Extra><![CDATA[" + prodName + "]]></Extra>";
        xml += "<Products>";

            xml += "<Product>";
            xml += "<ProdName><![CDATA[" + prodName + "]]></ProdName>";
            xml += "<ProdUnit>" + "" + "</ProdUnit>";
            xml += "<ProdQuantity>" + 1 + "</ProdQuantity>";
            xml += "<ProdPrice>" + total + "</ProdPrice>";
            xml += "<Amount>" + total + "</Amount>";
//            xml += "<Extra><![CDATA[" + prodName + "]]></Extra>";
            xml += "</Product>";

        xml += "</Products>";
        xml += "<KindOfService><![CDATA[" + "" + "]]></KindOfService>";
        xml += "<Total>" + total + "</Total>";
        xml += "<Amount>" + amount + "</Amount>";
        xml += "<AmountInWords><![CDATA[" + amountinwords + "]]></AmountInWords>";
        xml += "</Invoice>";
        xml += "</Inv>";
        xml += "</Invoices>";
        return xml;
    }

    private static String docHangChuc(double so, boolean daydu) {
        String chuoi = "";
        int chuc = (int) Math.floor(so / 10);
        int donvi = (int) so % 10;
        if (chuc > 1) {
            chuoi = " " + MANG_SO[chuc] + " mươi";
            if (donvi == 1) {
                chuoi += " mốt";
            }
        } else if (chuc == 1) {
            chuoi = " mười";
            if (donvi == 1) {
                chuoi += " một";
            }
        } else if (daydu && donvi > 0) {
            chuoi = " lẻ";
        }
        if (donvi == 5 && chuc >= 1) {
            chuoi += " lăm";
        } else if (donvi > 1 || (donvi == 1 && chuc == 0)) {
            chuoi += " " + MANG_SO[donvi];
        }
        return chuoi;
    }

    // Đọc block 3 số
    private static String docBlock(double so, boolean daydu) {
        String chuoi = "";
        int tram = (int) Math.floor(so / 100);
        so = so % 100;
        if (daydu || tram > 0) {
            chuoi = " " + MANG_SO[tram] + " trăm";
            chuoi += docHangChuc(so, true);
        } else {
            chuoi = docHangChuc(so, false);
        }
        return chuoi;
    }

    // Đọc số hàng triệu
    private static String docHangTrieu(double so, boolean daydu) {
        String chuoi = "";
        int trieu = (int) Math.floor(so / 1000000);
        so = so % 1000000;
        if (trieu > 0) {
            chuoi = docBlock(trieu, daydu) + " triệu";
            daydu = true;
        }
        double nghin = Math.floor(so / 1000);
        so = so % 1000;
        if (nghin > 0) {
            chuoi += docBlock(nghin, daydu) + " nghìn";
            daydu = true;
        }
        if (so > 0) {
            chuoi += docBlock(so, daydu);
        }
        return chuoi;
    }

    // Đọc số
    public static String docSo(double so) {
        if (so == 0)
            return MANG_SO[0];
        String chuoi = "", hauto = "";
        do {
            double ty = so % 1000000000;
            so = Math.floor(so / 1000000000);
            if (so > 0) {
                chuoi = docHangTrieu(ty, true) + hauto + chuoi;
            } else {
                chuoi = docHangTrieu(ty, false) + hauto + chuoi;
            }
            hauto = " tỷ";
        } while (so > 0);
        try {
            if (chuoi.trim().substring(chuoi.trim().length() - 1, 1).equals(",")) {
                chuoi = chuoi.trim().substring(0, chuoi.trim().length() - 1);
            }
        } catch (Exception e) {
        }
        chuoi = chuoi.trim().substring(0, 1).toUpperCase() + chuoi.trim().substring(1, chuoi.trim().length());
        return chuoi.trim() + " đồng";
    }

    private static String genKey (String userName){
        String key = "BL" + userName + System.currentTimeMillis();
        return key;
    }
}
