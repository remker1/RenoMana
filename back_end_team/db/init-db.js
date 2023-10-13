// This file initializes database with a table mango_tb
db = db.getSiblingDB("mango_db");
db.mango_tb.drop();

db.mango_tb.insertMany([
    {
        "id": 1,
        "name": "Bob",
        "role": "employee"
    },
    {
        "id": 2,
        "name": "Steven",
        "role": "manager"
    },
    {
        "id": 3,
        "name": "Sam",
        "role": "owner"
    },
]);