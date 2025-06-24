package com.bank;

import java.io.IOException;

import com.customer.Customer;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


//checks for user authentication for login into the account
@WebServlet("/bank/logincredentialscheck")
public class CredentialsCheck extends HttpServlet {
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        response.setContentType("text/html");
        
    	//getting the user entered data when loging in
        String phoneNumber = request.getParameter("phoneNumber");
        String accountHolderName= request.getParameter("accHolderName");
        String pin = request.getParameter("pin");

        // actual db code for user authentication validation
        UserDataHandler loginCheck = new UserDataHandler();
        
        Customer customer = null;
        
        // if user data exsists then entered otherwise it shows invalid credentials
        if (loginCheck.check(accountHolderName,phoneNumber, pin))
        {
            customer = loginCheck.getCustomerData();

            
	        if (customer != null) {
	            HttpSession session = request.getSession();
	            session.setAttribute("userdata", customer);  //sets session by user data
	            response.sendRedirect(request.getContextPath() + "/home.jsp"); // redirecting to the home page
	            return;
	        }
        }
        else
        {
            request.setAttribute("errorMessage", "Invalid Credentials"); 
            RequestDispatcher rd = request.getRequestDispatcher("../index.jsp"); // redirecting to index page because entered some wrong data
            rd.forward(request, response);
        }
    }
}
