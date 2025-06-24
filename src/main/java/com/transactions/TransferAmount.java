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

@WebServlet("/transactions/transferamount")
public class TransferAmount extends HttpServlet
{
	DAOClass daoClass;
	UserDataHandler loginDao;
	Customer customer;
	Bank admin;

	public TransferAmount()  {
		daoClass = new DAOClass();
		loginDao = new UserDataHandler();
		customer = new Customer();
		admin=new Bank();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession session = request.getSession();
		Customer currentUser = (Customer) session.getAttribute("userdata");  // getting current userdata for to know the current balance
		double currentBalance=currentUser.getBalanceAmount(); // getting the current balance
		int id = currentUser.getId(); // this is for to update current balance

		String amount = request.getParameter("amount");  // getting the user amount in string 
		double transferAmount=Double.valueOf(amount);   // tycasting the amount from string to double
 

		// if the current user has less amount than transferred amount then it'll shows current user available amount otherwise checks whether the transferred user is exsisted or not
		if(currentBalance>=transferAmount)
		{
			    // this is the transferred person account details
		        String accountholdername=request.getParameter("accountholdername");
				String accountNumber = request.getParameter("accountnumber");
				String cardnumber=request.getParameter("cardnumber");
				String ifsccode=request.getParameter("ifsccode");
				String phonenumber=request.getParameter("phonenumber");

				// the transferred person details has been verified
			  	TransferAccountHandler transferhandler=new TransferAccountHandler();
			  	
			  	//if the transferred user is verified and after updation then the current user amount have been updated which means ''currentBalance-TransferredAmount''
				if(transferhandler.updateTransferAccountDetails(transferAmount, accountholdername.toUpperCase(), accountNumber, cardnumber, ifsccode, phonenumber)) 
				{
				// query
				String quary = "UPDATE userdata SET balance=?  WHERE accountholdername=? and accountnumber=? and ifsccode=?";
				try {

					currentBalance=currentBalance-transferAmount;
					PreparedStatement st = (daoClass.getConnection()).prepareStatement(quary);
					st.setDouble(1, currentBalance);
					st.setString(2, currentUser.getAccountHolderName().toUpperCase());
					st.setString(3, currentUser.getAccountNumber());
					st.setString(4, currentUser.getIFSCCode());

              
					st.executeUpdate();
	                //loginDao.fetchById(id);

	                currentUser.setBalanceAmount(currentBalance);
	                //admin.updateAmountAfterTransfer(currentUser, transferAmount);
	                session.setAttribute("userdata", currentUser);
					session.setAttribute("successMessage", transferAmount+" Rupees Transferred Successful! ");
					response.sendRedirect(request.getContextPath() + "/home.jsp");


				} catch (SQLException e)
				{
					e.printStackTrace();
			    }
			}
		    else {
		    	request.setAttribute("errorMessage","User data not found!");
		    	RequestDispatcher rd = request.getRequestDispatcher("transferAmount.jsp");
			    rd.forward(request, response);
		    }

	   }
	   else
	   {
			request.setAttribute("errorMessage","Your current balance is "+ currentBalance);
			RequestDispatcher rd = request.getRequestDispatcher("transferAmount.jsp");
		    rd.forward(request, response);

	   }
    }
}
