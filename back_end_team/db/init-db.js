// This file initializes the database with a collections for our necessary tables
db = db.getSiblingDB("mango_db");

// Reset existing collections
db.employees.drop();
db.inventory.drop();
db.projects.drop();
db.user.drop();


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
        "id": ObjectId("101"),
        "name": "Wrench_1",
        "serial_number": "ABC12345",
        "manufacturer": "Craftsman",
        "warranty": "2023/10/19"
    },
    {
        "id": ObjectId("102"),
        "name": "Wrench_2",
        "serial_number": "XYZ98765",
        "manufacturer": "Snap-on",
        "warranty": "2023/11/19"
    },
    {
        "id": ObjectId("103"),
        "name": "Screwdriver_1",
        "serial_number": "PQR56789",
        "manufacturer": "Stanley",
        "warranty": "2023/06/19"
    },
    {
        "id": ObjectId("104"),
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
        "client": "Henry John Smith",
        "end_date": "2023-12-31",
        "location": "123 Main Street"
        "inventory_item(s)": [ObjectId(101)]
    },
    {
        "id": 202,
        "name": "Office Expansion",
        "client": "ABC Corporation",
        "end_date": "2024-02-15",
        "location": "456 Business Avenue"
        "inventory_item(s)": [ObjectId(102)]
    },
    {
        "id": 203,
        "name": "Garden Landscaping",
        "client": "Green Thumb Gardeners",
        "end_date": "2023-10-15",
        "location": "789 Garden Road"
        "inventory_item(s)": [ObjectId(103)]
    },
    {
       "id": 204,
       "name": "Office Furnishing",
       "client": "Tech Solutions Inc.",
       "end_date": "2023-11-30",
       "location": "456 Business Avenue",
       "inventory_item(s)": [ObjectId("item_003"), ObjectId("item_004")]
    },
]);

db.users.insertOne({
    "id": ObjectId(),
    "username": "newUser",
    "password": "hashedPassword",
});

// Create a registration process in your application to collect user data and save it to the database.
// Implement password hashing for security.

// Implement a login mechanism where users provide their username and password.

// Pseudo-code for login and access control
const username = "user_input_username"; // Replace with user input
const password = "user_input_password"; // Replace with user input

const user = db.users.findOne({ username });

if (user) {
    // Check if the provided password matches the stored hashed password
    if (validatePassword(password, user.password))
    {
        // User is authenticated, retrieve their role and permissions
        const role = user.role;

        if (role == "manager") {
            // Grant access to collections based on role
            // Manager can access employees, inventory, and projects
            // Contractor may have limited access
        }
        else
        {
            // Handle access for other roles (e.g., contractor)
        }
    }
    else
    {
        // Incorrect password
        // Handle login failure
    }
}
else
    {
        // User not found
        // Handle login failure
    }

function validatePassword(inputPassword, storedHashedPassword) {
    // Implement password validation logic, e.g., using bcrypt
    // Compare the input password with the stored hashed password
}
