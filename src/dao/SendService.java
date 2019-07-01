package dao;

import entity.MobileCard;

public interface SendService {
    int send(int count, MobileCard card) throws Exception;
}
