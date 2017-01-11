/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.com.codefire.dao;

import java.util.List;
import javax.swing.ImageIcon;
import ua.com.codefire.dao.entity.Contact;

/**
 *
 * @author codefire
 */
public interface PhonesDao {
    List<Contact> getAll();
    List<Contact> find(String name);
    void add(Contact contact, String destination);
    void delete(int id);
    ImageIcon getImage(int id);
    void update(Contact contact, String destination);
}
