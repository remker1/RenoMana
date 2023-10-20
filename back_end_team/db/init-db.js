// This file initializes the database with a collections for our necessary tables
db = db.getSiblingDB("mango_db");

// Reset existing collections
db.employees.drop();
db.inventory.drop();
db.projects.drop();


// Creation of the "employees" collection
db.employees.insertMany([
    {
        "id": 1,
        "first_name": "Bob",
        "last_name": "Smith",
        "cell_number": "123-456-7890",
        "employee_type": "Contractor"
    },
    {
        "id": 2,
        "first_name": "Steven",
        "last_name": "Johnson",
        "cell_number": "987-654-3210",
        "employee_type": "Contractor"
    },
    {
        "id": 3,
        "first_name": "Sam",
        "last_name": "Davis",
        "cell_number": "555-555-5555",
        "employee_type": "Contractor"
    },
]);

// Creation of the "inventory" collection
db.inventory.insertMany([
    {
        "id": 101,
        "name": "Wrench_1",
        "serial_number": "ABC12345",
        "manufacturer": "Craftsman",
        "warranty": "2023/10/19"
    },
    {
        "id": 102,
        "name": "Wrench_2",
        "serial_number": "XYZ98765",
        "manufacturer": "Snap-on",
        "warranty": "2023/11/19"
    },
    {
        "id": 103,
        "name": "Screwdriver_1",
        "serial_number": "PQR56789",
        "manufacturer": "Stanley",
        "warranty": "2023/06/19"
    },
    {
        "id": 103,
        "name": "PipeBender_1",
        "serial_number": "PQR56789",
        "manufacturer": "IRWIN Tools",
        "warranty": "2023/06/19"
     },
]);

// Creation of "projects" collection
db.projects.insertMany([
    {
        "id": 201,
        "name": "Smith bathroom renovation",
        "client": "Henry Smith",
        "end_date": "2023-12-31",
        "location": "123 Main Street"
    },
    {
        "id": 202,
        "name": "Office Expansion",
        "client": "ABC Corporation",
        "end_date": "2024-02-15",
        "location": "456 Business Avenue"
    },
    {
        "id": 203,
        "name": "Garden Landscaping",
        "client": "Green Thumb Gardeners",
        "end_date": "2023-10-15",
        "location": "789 Garden Road"
    }
]);