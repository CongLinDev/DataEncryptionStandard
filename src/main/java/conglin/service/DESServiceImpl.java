package conglin.service;

import conglin.util.DESUtil;

public class DESServiceImpl implements DESService{
    private DESUtil desUtil = new DESUtil();

    private long[] getText(String block){
        byte[] byteBlock = block.getBytes();

        long longBlock[] = new long[(byteBlock.length >> 3) + 1];

        for(int i = 0, j = -1; i < byteBlock.length; i++){
            if((i & 7) == 0)//如果i % 8 == 0
                j++;
            longBlock[j] <<= 8;
            longBlock[j] |= byteBlock[i];
        }
        return longBlock;
    }

    private long getKey(String key){
        byte[] byteKey = key.getBytes();
        long longKey = 0;
        for (byte bk : byteKey){
            longKey <<= 8;
            longKey |= bk;
        }
        return longKey;
    }

    private String longToString(long longValue){
        byte [] byteArray = new byte[4];

        byteArray[0] = (byte)(longValue >> 24);
        byteArray[1] = (byte)(longValue >> 16);
        byteArray[2] = (byte)(longValue >> 8);
        byteArray[3] = (byte)(longValue);
        
        return new String(byteArray);
    }
    /**
     * 加密
     */
    @Override
    public String encrypt(String block, String key){
        long[] longBlock = getText(block);
        long longKey = getKey(key);
        StringBuilder stringBuilder = new StringBuilder();
        for(long lb : longBlock){
            stringBuilder.append(longToString(encrypt(lb, longKey)));
        }
        return stringBuilder.toString();
    }

    /**
     * 加密
     */
    @Override
    public long encrypt(long block, long key){
        return cipher(block, key, true);
    }

    /**
     * 解密
     */
    @Override
    public String decrypt(String block, String key){
        long[] longBlock = getText(block);
        long longKey = getKey(key);
        StringBuilder stringBuilder = new StringBuilder();
        for(long lb : longBlock){
            stringBuilder.append(Long.toString(decrypt(lb, longKey)));
        }
        return stringBuilder.toString();
    }

    /**
     * 解密
     */
    @Override
    public long decrypt(long block, long key){
        return cipher(block, key, false);
    }

    /**
     * @param block 加密或解密的信息
     * @param key 密钥
     * @param encrypt true为加密 false为解密
     * @return 返回密文或明文
     */
    private long cipher(long block, long key, boolean encrypt){
        //生成轮询子密钥
        long[] roundSubkeys = desUtil.generateRoundSubkeys(key);
        //初始置换
        block = desUtil.initialPermutation(block);

        int leftHalf = (int) (block >> 32);
        int rightHalf = (int) block;

        int output = 0;

        //16轮 轮询
        for (int i = 0; i < DESUtil.NUM_OF_ROUNDS; i++){
            //右半部分进行F运算
            if(encrypt)
                output = desUtil.coreFunction(rightHalf, roundSubkeys[i]);
            else
                output = desUtil.coreFunction(rightHalf, roundSubkeys[DESUtil.NUM_OF_ROUNDS-1-i]);
            //左半部分与右半部分异或得到新的右半部分（但此时保存在左半部分） 
            leftHalf ^= output;
            //交换左右部分
            leftHalf ^= rightHalf;
    		rightHalf ^= leftHalf;
    		leftHalf ^= rightHalf;
        }
        //合并左右部分
        long joinedHalves = ((rightHalf & DESUtil.MASK_32_BITS) << 32 | (leftHalf & DESUtil.MASK_32_BITS));
        //初始逆置换
        return desUtil.initialInversePermutation(joinedHalves);
    }

}