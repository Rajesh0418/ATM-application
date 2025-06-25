package com.bank;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.customer.Customer;
import com.dao.DAOClass;

// the actual authentication was happend here
public class UserDataHandler {
    DAOClass loginDao;
    Customer customer;

    public UserDataHandler() {
        loginDao = new DAOClass();
        customer = new Customer();
    }

    public boolean check(String accountHolderName, String phoneNumber,  String pin) 
    {
    	//gets all data of the user from db
        String query = "select * from userdata where accountholdername=? and phonenumber=? and password=?";
        try {
            PreparedStatement st = (loginDao.getConnection()).prepareStatement(query);
            st.setString(1, accountHolderName.toUpperCase().trim());
            st.setString(2, phoneNumber);
            st.setString(3, pin);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                // Setting customer details to the customer object(Core java object)
                customer.setId(rs.getInt("id"));
                customer.setAccountHolderName(accountHolderName.toUpperCase());
                customer.setIFSCCode(rs.getString("ifsccode"));
                customer.setCardNumber(rs.getString("cardnumber"));
                customer.setAccountNumber(rs.getString("accountnumber"));
                customer.setEmail(rs.getString("email"));
                customer.setPhoneNumber(rs.getString("phonenumber"));
                customer.setAddress(rs.getString("address"));
                customer.setPassword(rs.getString("password"));
                customer.setBalanceAmount(rs.getDouble("balance"));
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // fetching the userdata by user id 
    public boolean fetchById(int id) {
    	String query = "select * from userdata where id=?";
        try {
            PreparedStatement st = (loginDao.getConnection()).prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                // Setting customer details
                customer.setId(rs.getInt("id"));
                customer.setAccountHolderName((rs.getString("accountholdername")).toUpperCase());
                customer.setIFSCCode(rs.getString("ifsccode"));
                customer.setCardNumber(rs.getString("cardnumber"));
                customer.setAccountNumber(rs.getString("accountnumber"));
                customer.setEmail(rs.getString("email"));
                customer.setPhoneNumber(rs.getString("phonenumber"));
                customer.setAddress(rs.getString("address"));
                customer.setPassword(rs.getString("password")); // This is the hashed PIN from DB
                customer.setBalanceAmount(rs.getDouble("balance"));

                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
        }

    // this menthod is called after setting the customer data to the Customer object for display on the page
    public Customer getCustomerData() {
        return customer;
    }
}
