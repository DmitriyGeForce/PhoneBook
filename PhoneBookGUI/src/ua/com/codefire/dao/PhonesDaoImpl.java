/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.com.codefire.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import ua.com.codefire.dao.entity.Contact;

/**
 *
 * @author codefire
 */
public class PhonesDaoImpl implements PhonesDao {

    private static final String SELECT_QUERY = "SELECT * FROM main.phones";

    private final String database;

    public PhonesDaoImpl(String database) {
        this.database = database;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + database);
    }

    @Override
    public List<Contact> getAll() {
        List<Contact> list = new ArrayList<>();
        try (Connection connect = getConnection()) {
            Statement state = connect.createStatement();
            ResultSet result = state.executeQuery(SELECT_QUERY);
            while (result.next()) {
                list.add(new Contact(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("phone"))
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(PhonesDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public List<Contact> find(String name) {
        List<Contact> list = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String query = "SELECT id, name, phone FROM main.phones WHERE name LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + name + "%");
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                list.add(new Contact(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("phone"))
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(PhonesDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public void add(Contact contact, String destination) {
        File file = new File(destination);
        try (Connection connection = getConnection();
                FileInputStream inputStream = new FileInputStream(file)) {
            String query = "INSERT INTO main.phones (name, phone, photo) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, contact.getName());
            preparedStatement.setString(2, contact.getPhone());
            preparedStatement.setBinaryStream(3, inputStream, (int) file.length());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(PhonesDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(int id) {
        try (Connection connection = getConnection()) {
            String query = ("DELETE FROM main.phones WHERE id=?");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PhonesDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ImageIcon getImage(int id) {
        ImageIcon image = null;
        try (Connection connection = getConnection()) {
            String query = "SELECT photo FROM phones WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            byte[] bs = resultSet.getBytes("photo");
            image = new ImageIcon(bs);
        } catch (SQLException ex) {
            Logger.getLogger(PhonesDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }

    @Override
    public void update(Contact contact, String destination) {
        try (Connection con = getConnection()) {
            String query = "UPDATE main.phones SET name = ?, phone = ? WHERE id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, contact.getName());
            preparedStatement.setString(2, contact.getPhone());
            preparedStatement.setInt(3, contact.getId());
            preparedStatement.executeUpdate();
            if (destination != null) {
                File file = new File(destination);
                try (FileInputStream fis = new FileInputStream(file)) {
                String queryImage = "UPDATE main.phones SET photo = ? WHERE id = ?";
                PreparedStatement preparedStatement1 = con.prepareStatement(queryImage);
                preparedStatement1.setBinaryStream(1, fis, (int) file.length());
                preparedStatement1.setInt(2, contact.getId());
                preparedStatement1.executeUpdate();
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(PhonesDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PhonesDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
