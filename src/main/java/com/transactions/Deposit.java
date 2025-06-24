package com.transactions;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.bank.Bank;
import com.bank.UserDataHandler;
import com.customer.Customer;
import com.dao.DAOClass;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//user deposit amount from an account
@WebServlet("/transactions/deposit")
public class Deposit extends HttpServlet
{
	DAOClass daoClass;
	UserDataHandler loginDao;
	Customer customer;
	Bank bank;

	public Deposit()  {
		daoClass = new DAOClass();
		loginDao = new UserDataHandler();
		customer = new Customer();
		bank=new Bank();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{

	    HttpSession session = request.getSession();
        Customer user = (Customer) session.getAttribute("userdata");
        //gets current balance of the user
	    double currentBalance = user.getBalanceAmount();

	    // Read form values
	    String accountholdername = request.getParameter("accountholdername");
	    String accountNumber = request.getParameter("accountnumber");
	    String ifsccode = request.getParameter("ifsccode");
	    String amount = request.getParameter("amount");

	    
	    double depositeAmount = Double.parseDouble(amount);        
        currentBalance +=depositeAmount;

        // updates the user current balance with deposited amount ''Total balance= currentBalance + DepositAmount''
	    bank.updateDepositAmount(user, depositeAmount);    
	    String query = "UPDATE userdata SET balance=? WHERE accountholdername=? and accountnumber=? and ifsccode=?";

	    try 
	    {
	    	PreparedStatement st = daoClass.getConnection().prepareStatement(query);
	        st.setDouble(1, currentBalance);
	        st.setString(2, accountholdername.toUpperCase());
	        st.setString(3, accountNumber);
	        st.setString(4, ifsccode);
	        st.executeUpdate();

	        session.setAttribute("userdata",user);
	        session.setAttribute("successMessage", depositeAmount + " Rupees Deposited Successfully!");
	        response.sendRedirect(request.getContextPath() + "/home.jsp");

	    } catch (SQLException e) {
	        e.printStackTrace();
	        response.sendRedirect(request.getContextPath() + "/error.jsp");
	    }
	}

}
