package com.vnpt.database;

/**
 * Define hang so trong chuong trinh
 * 
 * @author: Truonglt2
 * @version: 1.0
 * @since: 1.0
 */
public interface ConstantsDatabase {
	String CUSTOMER_TABLE = "Customer";
	String INVOICE_TABLE = "Invoice";
	String USER_TABLE = "User";
	// get all information catagory
    String queryAllTable = "SELECT * FROM ";
}
