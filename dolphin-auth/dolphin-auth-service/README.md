```
目前存在问题，refreshToken没有办法手工revoke掉，
那么，如果用户token过期，refreshToken未过期时，refreshToken将继续
生效，于是，如果用户refreshToken被盗取，那么相当于在整个refreshToken
有效期内，它都将拥有权限。

解决思路：
    在mysql中持久化用户生成的token与refreshToken
    当用户重新认证时，取出旧token，清除两个token，再生成新的，同时更新数据库中的token
    与refreshToken
    当用户刷新token，操作同1
    当用户修改密码时，直接清空两个token
```