package com.example.demo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.entity.OrderEntity;
import com.example.demo.entity.OrderEntity.OrderStatus;
import com.example.demo.entity.OrderItemEntity;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class EmailService{
	private final JavaMailSender mailSender;

	
	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
//	Simple text email fororder confirmation
	public void sendOrderConfirmation(OrderEntity order) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(order.getUser().getEmail());
		message.setSubject("Order Confirmation- Order #"+order.getId());
		StringBuilder itemsText = new StringBuilder();
		for (OrderItemEntity item:order.getItems()) {
			itemsText.append("- ")
			          .append(item.getProduct().getProductname())
			          .append("(Qty: ").append(item.getQuantity())
			          .append(", Price : Rs.").append(item.getPriceAtPurchase())
			          .append(")\n");
		}
			
			message.setText(
					"Dear "+ order.getUser().getName()+",\n\n" +
			         "Thank you foryour irder!\n\n"+
					 "Order Details:\n"+
			         "Order ID: #"+order.getId()+"\n"+
					 "Total Amount: Rs."+ order.getTotalAmount()+"\n"+
			          "Order Date: "+ order.getOrderDate()+ "\n"+
			          "Status: " + order.getStatus() + "\n\n" +
			            "Order Items:\n" + itemsText.toString() + "\n" +
			            "We will notify you when your order ships.\n\n" +
			            "Best regards,\n" +
			            "Your Store Team"
					 );
			
			mailSender.send(message);
		}
			
//			HTML email
		    public void sendOrderConfirmationHtml(OrderEntity order) {
		        try {
		            MimeMessage mimeMessage = mailSender.createMimeMessage();
		            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		            
		            helper.setTo(order.getUser().getEmail());
		            helper.setSubject("Order Confirmation - Order #" + order.getId());
		            
		            // Build HTML content manually
		            StringBuilder itemsHtml = new StringBuilder();
		            for (OrderItemEntity item : order.getItems()) {
		                itemsHtml.append("<li>")
		                        .append(item.getProduct().getProductname())
		                        .append(" - Quantity: ").append(item.getQuantity())
		                        .append(" - Price: ₹").append(item.getPriceAtPurchase())
		                        .append("</li>");
		            }
		            
		            String htmlContent = "<!DOCTYPE html>" +
		                "<html>" +
		                "<head><title>Order Confirmation</title></head>" +
		                "<body style='font-family: Arial, sans-serif;'>" +
		                "<h2>Order Confirmation</h2>" +
		                "<p>Dear " + order.getUser().getName() + ",</p>" +
		                "<p>Thank you for your order! Here are your order details:</p>" +
		                "<h3>Order #" + order.getId() + "</h3>" +
		                "<p><strong>Order Date:</strong> " + order.getOrderDate() + "</p>" +
		                "<p><strong>Total Amount:</strong> ₹" + order.getTotalAmount() + "</p>" +
		                "<p><strong>Status:</strong> " + order.getStatus() + "</p>" +
		                "<h4>Order Items:</h4>" +
		                "<ul>" + itemsHtml.toString() + "</ul>" +
		                "<p>We will notify you when your order ships.</p>" +
		                "<p>Best regards,<br>Your Store Team</p>" +
		                "</body>" +
		                "</html>";
		            
		            helper.setText(htmlContent, true);
		            mailSender.send(mimeMessage);
		        } catch (MessagingException e) {
		            System.err.println("Failed to send HTML order confirmation: " + e.getMessage());
		            // Fallback to text email
		            sendOrderConfirmation(order);
		        }
		    }

		    // Send status update emails
		    public void sendStatusUpdate(OrderEntity order, OrderStatus oldStatus, OrderStatus newStatus) {
		        try {
		            SimpleMailMessage message = new SimpleMailMessage();
		            message.setTo(order.getUser().getEmail());
		            message.setSubject("Order Status Update - Order #" + order.getId());
		            
		            String statusMessage = getStatusMessage(newStatus);
		            
		            message.setText(
		                "Dear " + order.getUser().getName() + ",\n\n" +
		                "Your order status has been updated:\n\n" +
		                "Order ID: #" + order.getId() + "\n" +
		                "Old Status: " + oldStatus + "\n" +
		                "New Status: " + newStatus + "\n\n" +
		                statusMessage + "\n\n" +
		                "Best regards,\n" +
		                "Your Store Team"
		            );
		            
		            mailSender.send(message);
		        } catch (Exception e) {
		            System.err.println("Failed to send status update email: " + e.getMessage());
		        }
		    }

		    private String getStatusMessage(OrderStatus status) {
		        switch (status) {
		            case PAID: return "Your payment has been confirmed and your order is being processed.";
		            case PROCESSING: return "Your order is now being processed and prepared for shipment.";
		            case SHIPPED: return "Your order has been shipped! Tracking information will follow.";
		            case DELIVERED: return "Your order has been delivered. Thank you for shopping with us!";
		            case CANCELLED: return "Your order has been cancelled as requested.";
		            default: return "Your order status has been updated.";
		        }
		    }

		    // Payment success email
		    public void sendPaymentSuccess(OrderEntity order) {
		        sendOrderConfirmationHtml(order); // Use HTML version for payment success
		    }

		    // Quick test method
		    public void sendTestEmail(String toEmail) {
		        try {
		            SimpleMailMessage message = new SimpleMailMessage();
		            message.setTo(toEmail);
		            message.setSubject("Test Email from E-commerce App");
		            message.setText("This is a test email to verify your email configuration is working!");
		            
		            mailSender.send(message);
		            System.out.println("Test email sent to: " + toEmail);
		        } catch (Exception e) {
		            System.err.println("Failed to send test email: " + e.getMessage());
		        }
		    }
		}