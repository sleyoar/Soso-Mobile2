package dao;

import entity.MobileCard;

public interface NetService {
    int netPlay(int flow, MobileCard card) throws Exception;
}
