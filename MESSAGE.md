Validasi ada di level aplikasi

1. login username(String) password(String)
* kedua String tsb. di-enkripsi oleh MD5

2. success id_user(int)
* mengembalikan message success setelah client berhasil login

3. fail
* mengembalikan message fail apabila client gagal terkoneksi

4. request id_user(int)
* meminta list of task yang dimiliki oleh id_user tsb.

5. update id_task(int) status(int) time(timestamp)
* mengupdate status suatu task

6. listtask isinya(String)

7. logout id_user(int)
* id_user logout

8. exit
* mengembalikan message exit sebagai response dari logout