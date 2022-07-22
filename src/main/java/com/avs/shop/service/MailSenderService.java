package com.avs.shop.service;

import com.avs.shop.domain.User;

public interface MailSenderService {

    void sendActivateCode(User user);

}
