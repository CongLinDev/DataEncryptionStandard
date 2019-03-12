# DataEncryptionStandard

## 使用

* 命令 `encrypt filename` 负责加密数据。
* 命令 `decrypt filename` 负责解密数据。
* 命令 `output filename` 负责输出数据。
* 命令 `exit` 退出。

## 文件格式

文件格式为 **json**，模板如下：

```json
{
    "text": 123456789,
    "keys": [123456, 789456]
}
```