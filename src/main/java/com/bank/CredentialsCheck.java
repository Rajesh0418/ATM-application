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


@WebServlet("/bank/logincredentialscheck")
public class CredentialsCheck extends HttpServlet {
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String phoneNumber = request.getParameter("phoneNumber");
        String accountHolderName= request.getParameter("accHolderName");
        String pin = request.getParameter("pin");

        UserDataHandler loginCheck = new UserDataHandler();
        
        Customer customer = null;
        if (loginCheck.check(accountHolderName,phoneNumber, pin))
        {
            customer = loginCheck.getCustomerData();

	        if (customer != null) {
	            HttpSession session = request.getSession();
	            session.setAttribute("userdata", customer);
	            response.sendRedirect(request.getContextPath() + "/home.jsp");
	            return;
	        }
        }
        else
        {
            request.setAttribute("errorMessage", "Invalid Credentials");
            RequestDispatcher rd = request.getRequestDispatcher("../index.jsp");
            rd.forward(request, response);
        }
    }
}
