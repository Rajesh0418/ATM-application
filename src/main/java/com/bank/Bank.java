package com.bank;

import com.customer.Customer;

// Bank operations
public class Bank
{
	    // this is method is used to change the current obj details as the user entered and showing it to the interface that how they entered
	    public Customer changeData(Customer customer,String name, String email, String password, String phoneNumber, String address)
	    {
	         customer.setAccountHolderName(name.toUpperCase());
	         customer.setEmail(email);
	         customer.setPhoneNumber(phoneNumber);
	         customer.setAddress(address);
	         customer.setPassword(password);
             
	         return customer;
	    }

	    public void updateWithdrawlAmount(Customer customer,double withdrawlAmount) {
	    	customer.setBalanceAmount(customer.getBalanceAmount()-withdrawlAmount);
	    }

	    public void updateDepositAmount(Customer customer,double depositAmount) {
	    	customer.setBalanceAmount(customer.getBalanceAmount()+depositAmount);
	    }
	    
	    public void updateAmountAfterTransfer(Customer customer,double transferAmount) {
	    	customer.setBalanceAmount(customer.getBalanceAmount()-transferAmount);
	    }
}