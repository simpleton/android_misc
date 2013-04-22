## Testcases for testing behavior of concurrence operations to Sqlite in Android platform##

conclusion:

* If u want to operation DB in serval Thread, please use SQLiteOpenHelper.

* If u want to use it in severval process, plz use worker module or content provider which should be set with multiprocess=false.

** Only use one helper to operate db in one process.**



