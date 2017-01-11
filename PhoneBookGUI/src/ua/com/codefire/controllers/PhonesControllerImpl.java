/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.com.codefire.controllers;

import java.util.List;
import javax.swing.ImageIcon;
import ua.com.codefire.dao.PhonesDao;
import ua.com.codefire.dao.PhonesDaoImpl;
import ua.com.codefire.dao.entity.Contact;

/**
 *
 * @author codefire
 */
public class PhonesControllerImpl implements PhonesController {

    private final static PhonesDao dao;

    static {
        dao = new PhonesDaoImpl("database.db"); // or load from resources
    }

    @Override
    public List<Contact> getAllContacts() {
        return dao.getAll();
    }

    @Override
    public List<Contact> getContactByName(String name) {
        return dao.find(name);
    }

    @Override
    public void add(Contact contact, String destination) {
        dao.add(contact, destination);
    }

    @Override
    public void delete(int id) {
        dao.delete(id);
    }

    @Override
    public ImageIcon getImage(int id) {
        return dao.getImage(id);
    }

    @Override
    public void updateById(Contact contact, String destination) {
        dao.update(contact, destination);
    }
}
