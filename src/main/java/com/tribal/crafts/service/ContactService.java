package com.tribal.crafts.service;

import com.tribal.crafts.entity.Contact;
import com.tribal.crafts.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Transactional
    public Contact submitContactRequest(Contact contact) {
        return contactRepository.save(contact);
    }

    public List<Contact> getAllContactRequests() {
        return contactRepository.findAll();
    }

    @Transactional
    public void deleteContactRequest(Long id) {
        contactRepository.deleteById(id);
    }
}
