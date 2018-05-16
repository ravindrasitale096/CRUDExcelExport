package com.exportexcel.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.exportexcel.model.User;
import com.exportexcel.util.DbUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPHeaderCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;



public class UserDao {

    private Connection connection;
 // Writing to PDF
    private static final String FILE_NAME = "D://Files/itext.pdf";

    public UserDao() {
        connection = DbUtil.getConnection();
    }

    public void addUser(User user) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into users(firstname,lastname,dob,email) values (?, ?, ?, ? )");
            // Parameters start with 1
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setDate(3, new java.sql.Date(user.getDob().getTime()));
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int userId) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("delete from users where userid=?");
            // Parameters start with 1
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update users set firstname=?, lastname=?, dob=?, email=?" +
                            "where userid=?");
            // Parameters start with 1
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setDate(3, new java.sql.Date(user.getDob().getTime()));
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setInt(5, user.getUserid());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from users");
            while (rs.next()) {
                User user = new User();
                user.setUserid(rs.getInt("userid"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setDob(rs.getDate("dob"));
                user.setEmail(rs.getString("email"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
    
    /**
     * 
     * @return
     */
    public List<User> writeUsingIText() {
    	 List<User> users = new ArrayList<User>();
    	 
    	try {
            Document document = new Document();
            /**
             * Now, we will create table in PDF file
             */
            PdfPTable table = new PdfPTable(5); // Create 2 columns in table.
            PdfWriter.getInstance(document, new FileOutputStream(new File(FILE_NAME)));
            table.addCell("User Id");
            table.addCell("First Name");
            table.addCell("Last Name");
            table.addCell("DOB");
            table.addCell("Email");
            //open
            document.open();

                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select * from users");
                PdfPCell cell = new PdfPHeaderCell();
                
               // document.add(table);
                while (rs.next()) {
                    User user = new User();
                    user.setUserid(rs.getInt("userid"));
                    user.setFirstName(rs.getString("firstname"));
                    user.setLastName(rs.getString("lastname"));
                    user.setDob(rs.getDate("dob"));
                    user.setEmail(rs.getString("email"));
                    users.add(user);
                   /* PdfPCell cell1 = new PdfPCell(new Paragraph("User Id"));
                    PdfPCell cell2 = new PdfPCell(new Paragraph("Cell First Name"));
                    PdfPCell cell3 = new PdfPCell(new Paragraph("Last Name"));
                    PdfPCell cell4 = new PdfPCell(new Paragraph("DOB"));
                    PdfPCell cell5 = new PdfPCell(new Paragraph("Email"));*/
                    table.addCell(rs.getString("userid"));
                    table.addCell(rs.getString("firstname"));
                    table.addCell(rs.getString("lastname"));
                    table.addCell(rs.getString("dob"));
                    table.addCell(rs.getString("email"));
                    
                   
                }
                document.add(table);
                //close
                document.close();
            } catch (SQLException | FileNotFoundException | DocumentException e) {
                e.printStackTrace();
            }
         
        return users;
    }

    public User getUserById(int userId) {
        User user = new User();
        try {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("select * from users where userid=?");
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                user.setUserid(rs.getInt("userid"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setDob(rs.getDate("dob"));
                user.setEmail(rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}