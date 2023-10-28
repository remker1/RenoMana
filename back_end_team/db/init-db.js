// This file initializes the database with a collections for our necessary tables
db = db.getSiblingDB("mango_db");

// Reset existing collections
db.employees.drop();
db.inventory.drop();


db.employees.insertMany([
    {
        "id": 1,
        "first_name": "Bob",
        "last_name": "Smith",
        "cell_number": "123-456-7890",
        "employee_type": "contractor"
    },
    {
        "id": 2,
        "first_name": "Steven",
        "last_name": "Johnson",
        "cell_number": "987-654-3210",
        "employee_type": "contractor"
    },
    {
        "id": 3,
        "first_name": "Sam",
        "last_name": "Davis",
        "cell_number": "555-555-5555",
        "employee_type": "contractor"
    },
]);

// Creation of the "inventory" collection
