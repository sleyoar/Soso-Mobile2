package dao;
import entity.MobileCard;
public interface CallService {
    int call(int minCount, MobileCard card) throws Exception;
}
