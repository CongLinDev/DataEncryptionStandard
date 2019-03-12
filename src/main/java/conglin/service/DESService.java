package conglin.service;

public interface DESService{
    long encrypt(long block, long key);
    long decrypt(long block, long key);

    String encrypt(String block, String key);
    String decrypt(String block, String key);
}